/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2021, Thomas Kellerer.
 *
 * Licensed under a modified Apache License, Version 2.0
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
package workbench.sql.parser;

import workbench.WbTestCase;
import workbench.sql.DelimiterDefinition;
import workbench.sql.lexer.SQLToken;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Alfred Porter
 */
public class MySQLDelimiterTesterTest
  extends WbTestCase
{

  public MySQLDelimiterTesterTest()
  {
    super("MySQLDelimiterTesterTest");
  }

  @Test
  public void testOnlyReservedWordAtStartOfStatement()
  {
    MySQLDelimiterTester tester = new MySQLDelimiterTester();
    SQLToken token = new SQLToken(SQLToken.RESERVED_WORD, "DELIMITER", 0, 0);
    assertTrue(tester.isDelimiterCommand(token, true));
    assertFalse(tester.isDelimiterCommand(token, false));

    token = new SQLToken(SQLToken.IDENTIFIER, "DELIMITER", 0, 0);
    assertFalse(tester.isDelimiterCommand(token, true));
    token = new SQLToken(SQLToken.LITERAL_STRING, "DELIMITER", 0, 0);
    assertFalse(tester.isDelimiterCommand(token, true));
  }

  @Test
  public void testDelimiterUpdates()
  {
    final String testDelimiter = "$$";

    MySQLDelimiterTester tester = new MySQLDelimiterTester();
    assertEquals(DelimiterDefinition.STANDARD_DELIMITER, tester.getCurrentDelimiter());

    SQLToken token = new SQLToken(SQLToken.RESERVED_WORD, "DELIMITER", 0, 0);
    tester.currentToken(token, true);
    assertEquals(DelimiterDefinition.STANDARD_DELIMITER, tester.getCurrentDelimiter());
    token = new SQLToken(SQLToken.IDENTIFIER, testDelimiter, 0, 0);
    tester.currentToken(token, false);
    assertEquals(testDelimiter, tester.getCurrentDelimiter().toString());
    tester.lineEnd();
    tester.statementFinished();

    token = new SQLToken(SQLToken.RESERVED_WORD, "DELIMITER", 0, 0);
    tester.currentToken(token, true);
    token = new SQLToken(SQLToken.IDENTIFIER, ";", 0, 0);
    tester.currentToken(token, false);
    assertEquals(";", tester.getCurrentDelimiter().toString());
    tester.lineEnd();
    tester.statementFinished();
  }

  @Test
  public void testStatementParsing()
  {
    String sql =
      "DELIMITER $$\n" +
      "CREATE PROCEDURE sp_proc() BEGIN \n" +
      "  SELECT 1; \n" +
      "END\n" +
      "$$\n" +
      "DELIMITER ;\n" +
      "SELECT 1;\n";
    ScriptParser parser = new ScriptParser(ParserType.MySQL);
    parser.setScript(sql);
    int count = parser.getStatementCount();
    assertEquals(4, count);
    assertTrue(parser.getCommand(0).startsWith("DELIMITER"));
    assertTrue(parser.getCommand(1).startsWith("CREATE"));
    assertTrue(parser.getCommand(2).startsWith("DELIMITER"));
    assertTrue(parser.getCommand(3).startsWith("SELECT"));
  }
}
