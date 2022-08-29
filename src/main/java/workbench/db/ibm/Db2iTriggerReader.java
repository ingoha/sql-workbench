/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2022 Thomas Kellerer.
 *
 * Licensed under a modified Apache License, Version 2.0 (the "License")
 * that restricts the use for certain governments.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.sql-workbench.eu/manual/license.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.eu
 */
package workbench.db.ibm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import workbench.log.CallerInfo;
import workbench.log.LogMgr;

import workbench.db.DefaultTriggerReader;
import workbench.db.JdbcUtils;
import workbench.db.TableIdentifier;
import workbench.db.TriggerDefinition;
import workbench.db.WbConnection;

import workbench.storage.DataStore;

import workbench.util.StringUtil;

/**
 *
 * @author Thomas Kellerer
 */
public class Db2iTriggerReader
  extends DefaultTriggerReader
{
  private final char catalogSeparator;

  public Db2iTriggerReader(WbConnection conn)
  {
    super(conn);
    catalogSeparator = conn.getMetadata().getCatalogSeparator();
  }

  @Override
  public String getTriggerSource(String triggerCatalog, String triggerSchema, String triggerName, TableIdentifier triggerTable, String trgComment, boolean includeDependencies)
    throws SQLException
  {
    if (Db2GenerateSQL.useGenerateSQLProc(dbConnection, Db2GenerateSQL.TYPE_TRIGGER))
    {
      return retrieveTrigger(triggerSchema, triggerName);
    }
    return super.getTriggerSource(triggerCatalog, triggerSchema, triggerName, triggerTable, trgComment, includeDependencies);
  }

  public String retrieveTrigger(String schema, String name)
  {
    Db2GenerateSQL sql = new Db2GenerateSQL(dbConnection);
    sql.setGenerateRecreate(true);
    CharSequence source = sql.getTriggerSource(schema, name);
    return source == null ? "" : source.toString();
  }

  @Override
  protected DataStore getTriggers(String catalog, String schema, String tableName)
    throws SQLException
  {
    String sql =
      "SELECT trigger_schema, \n" +
      "       trigger_name, \n" +
      "       event_object_schema, \n" +
      "       event_object_table,\n" +
      "       action_timing as trigger_type,\n" +
      "       event_manipulation as trigger_event  \n" +
      "FROM qsys2" + catalogSeparator +"systriggers \n";

    final CallerInfo ci = new CallerInfo(){};
    boolean forTable = false;
    String type;
    if (StringUtil.isBlank(tableName))
    {
      type = "trigger list";
      sql += "WHERE trigger_schema = ? \n";
    }
    else
    {
      forTable = true;
      type = "table triggers";
      sql +=
        "WHERE event_object_schema = ? \n" +
        "  AND event_object_table = ? \n";
    }
    sql += "ORDER BY 1,2";

    LogMgr.logMetadataSql(ci, type, sql, schema, tableName);

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    DataStore result = createResultDataStore();
    try
    {
      pstmt = this.dbConnection.getSqlConnection().prepareStatement(sql);
      pstmt.setString(1, schema);
      if (forTable)
      {
        pstmt.setString(2, tableName);
      }
      rs = pstmt.executeQuery();
      while (rs.next())
      {
        String trgSchema = StringUtil.trim(rs.getString(1));
        String trgName = StringUtil.trim(rs.getString(2));
        String trgTableSchema = StringUtil.trim(rs.getString(3));
        String trgTableName = StringUtil.trim(rs.getString(4));
        String trgType = StringUtil.trim(rs.getString(5));
        String event = StringUtil.trim(rs.getString(6));

        TableIdentifier tbl = new TableIdentifier(trgTableSchema, trgTableName);
        int row = result.addRow();
        result.setValue(row, COLUMN_IDX_TABLE_TRIGGERLIST_TRG_NAME, trgName);
        result.setValue(row, COLUMN_IDX_TABLE_TRIGGERLIST_TRG_TYPE, trgType);
        result.setValue(row, COLUMN_IDX_TABLE_TRIGGERLIST_TRG_EVENT, event);
        result.setValue(row, COLUMN_IDX_TABLE_TRIGGERLIST_TRG_TABLE, tbl.getTableExpression(dbConnection));

        TriggerDefinition trg = new TriggerDefinition(catalog, trgSchema, trgName);
        trg.setRelatedTable(tbl);
        trg.setTriggerType(trgType);
        trg.setTriggerEvent(event);
        result.getRow(row).setUserObject(trg);
      }
    }
    catch (Exception ex)
    {
      LogMgr.logMetadataError(ci, ex, type, sql, schema, tableName);
    }
    finally
    {
      JdbcUtils.closeAll(rs, pstmt);
    }

    return result;
  }

}
