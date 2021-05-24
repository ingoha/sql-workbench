/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2021, Thomas Kellerer
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
package workbench.gui.actions;

import java.awt.event.ActionEvent;

import workbench.interfaces.FileActions;
import workbench.log.CallerInfo;
import workbench.log.LogMgr;

/**
 * Action to copy a connection profile in the connect dialog.
 *
 * @author Thomas Kellerer
 */
public class CopyProfileAction
  extends WbAction
{
  private FileActions client;

  public CopyProfileAction(FileActions aClient)
  {
    super();
    this.client = aClient;
    this.setIcon("copy-profile");
    this.initMenuDefinition("LblCopyProfile");
  }

  @Override
  public void executeAction(ActionEvent e)
  {
    try
    {
      this.client.newItem(true);
    }
    catch (Exception ex)
    {
      LogMgr.logError(new CallerInfo(){}, "Error copying profile", ex);
    }
  }

  @Override
  public boolean useInToolbar()
  {
    return false;
  }
}
