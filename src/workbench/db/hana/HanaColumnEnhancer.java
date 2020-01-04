/*
 * HsqlColumnEnhancer.java
 *
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2020, Thomas Kellerer
 *
 * Licensed under a modified Apache License, Version 2.0
 * that restricts the use for certain governments.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *     https://www.sql-workbench.eu/manual/license.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.eu
 *
 */
package workbench.db.hana;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import workbench.log.CallerInfo;
import workbench.log.LogMgr;

import workbench.db.ColumnDefinitionEnhancer;
import workbench.db.ColumnIdentifier;
import workbench.db.TableDefinition;
import workbench.db.WbConnection;

import workbench.util.SqlUtil;
import workbench.util.StringUtil;

/**
 * A class to retrieve information about computed/generated columns in Hana
 *
 * @author Thomas Kellerer
 */
public class HanaColumnEnhancer
  implements ColumnDefinitionEnhancer
{
  @Override
  public void updateColumnDefinition(TableDefinition table, WbConnection conn)
  {
    if (conn.getDbSettings().getBoolProperty(HanaTableDefinitionReader.PROP_USE_JDBC_GETCOLUMNS, false))
    {
      // if the JDBC driver is used to retrieve the column information we need to update
      // the autogenerated columns.
      updateComputedColumns(table, conn);
    }
  }

  private void updateComputedColumns(TableDefinition table, WbConnection conn)
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;

    String tablename = table.getTable().getRawTableName();
    String schema = table.getTable().getRawSchema();

    String sql =
      "select column_name, \n" +
      "       generation_type \n" +
      "from sys.table_columns \n" +
      "where table_name = ? \n" +
      "and schema_name = ? \n" +
      "and generation_type is not null";

    Map<String, String> expressions = new HashMap<>();

    try
    {
      stmt = conn.getSqlConnection().prepareStatement(sql);
      stmt.setString(1, tablename);
      stmt.setString(2, schema);
      rs = stmt.executeQuery();

      while (rs.next())
      {
        String colname = rs.getString(1);
        String generated = rs.getString(2);
        if (StringUtil.isNonEmpty(generated))
        {
          expressions.put(colname, "GENERATED " + generated);
        }
      }
    }
    catch (Exception e)
    {
      LogMgr.logError(new CallerInfo(){}, "Error retrieving computed columns", e);
    }
    finally
    {
      SqlUtil.closeAll(rs, stmt);
    }

    for (ColumnIdentifier col : table.getColumns())
    {
      String expr = expressions.get(col.getColumnName());
      if (StringUtil.isNonBlank(expr))
      {
        col.setDefaultValue(null);
        col.setComputedColumnExpression(expr);
        col.setIsAutoincrement(true);
      }
    }
  }

}
