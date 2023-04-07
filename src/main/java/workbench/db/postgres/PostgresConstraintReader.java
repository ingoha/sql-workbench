/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2023 Thomas Kellerer
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
package workbench.db.postgres;

import java.util.List;

import workbench.db.AbstractConstraintReader;
import workbench.db.ConstraintType;
import workbench.db.IndexDefinition;
import workbench.db.TableConstraint;
import workbench.db.TableIdentifier;

import workbench.util.CollectionUtil;


/**
 * Read table level constraints for Postgres.
 *
 * @author  Thomas Kellerer
 */
public class PostgresConstraintReader
  extends AbstractConstraintReader
{
  private final String TABLE_SQL =
      "-- SQL Workbench/J \n" +
      "select rel.conname,  \n" +
      "       pg_catalog.pg_get_constraintdef(rel.oid) as src, \n" +
      "       pg_catalog.obj_description(rel.oid) as remarks  \n" +
      "from pg_catalog.pg_class t \n" +
      "  join pg_catalog.pg_constraint rel on t.oid = rel.conrelid   \n" +
      "  join pg_catalog.pg_namespace nsp on t.relnamespace = nsp.oid \n" +
      "where rel.contype in ('c', 'x') \n" +
      " and t.relname = ? \n" +
      " and nsp.nspname = ? ";

  public PostgresConstraintReader(String dbId)
  {
    super(dbId);
  }


  @Override
  public String getColumnConstraintSql(TableIdentifier tbl)
  {
    return null;
  }

  @Override
  public String getTableConstraintSql(TableIdentifier tbl)
  {
    return TABLE_SQL;
  }

  @Override
  public int getIndexForTableNameParameter()
  {
    return 1;
  }

  @Override
  public int getIndexForSchemaParameter()
  {
    return 2;
  }

  @Override
  public void updateIndexList(List<TableConstraint> constraints, List<IndexDefinition> indexList)
  {
    if (CollectionUtil.isEmpty(constraints)) return;
    if (CollectionUtil.isEmpty(indexList)) return;

    for (TableConstraint cons : constraints)
    {
      if (cons.getConstraintType() == ConstraintType.Exclusion)
      {
        IndexDefinition index = IndexDefinition.findIndex(indexList, cons.getConstraintName(), null);
        if (index != null)
        {
          index.setAutoGenerated(true);
        }
      }
    }
  }

}
