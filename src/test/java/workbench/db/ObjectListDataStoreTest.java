/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2021 Thomas Kellerer.
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
package workbench.db;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Thomas Kellerer
 */
public class ObjectListDataStoreTest
{

  @Test
  public void testGetTableIdentifier()
  {
    ObjectListDataStore ds = new ObjectListDataStore();
    int row = ds.addRow();
    ds.setObjectName(row, "PERSON");
    ds.setType(row, "TABLE");
    ds.setCatalog(row, "MY_CAT");
    ds.setSchema(row, "PUBLIC");
    ds.setRemarks(row, "The heroes");
    TableIdentifier tbl = ds.getTableIdentifier(row);
    assertEquals("PERSON", tbl.getTableName());
    assertEquals("PUBLIC", tbl.getSchema());
    assertEquals("MY_CAT", tbl.getCatalog());
    assertEquals("TABLE", tbl.getType());
    assertEquals("The heroes", tbl.getComment());
  }

}
