/*
 * This file is part of SQL Workbench/J, http://www.sql-workbench.net
 *
 * Copyright 2002-2013, Thomas Kellerer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * To contact the author please send an email to: support@sql-workbench.net
 *
 */
package workbench.util;

import java.awt.Window;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import workbench.log.CallerInfo;
import workbench.log.LogMgr;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;
import workbench.workspace.WbWorkspace;

import workbench.gui.WbSwingUtilities;
import workbench.gui.YesNoCancel;
import workbench.gui.components.ExtensionFileFilter;
import workbench.gui.components.WbFileChooser;

/**
 *
 * @author Thomas Kellerer
 */
public class WorkspaceSelector {

  private final Window parentWindow;

  public WorkspaceSelector(Window parentWindow)
  {
    this.parentWindow = parentWindow;
  }

  public String showSaveDialog()
  {
    return getWorkspaceFilename(true);
  }

  public String showLoadDialog()
  {
    return getWorkspaceFilename(false);
  }

  private String getWorkspaceFilename(boolean toSave)
  {
    try
    {
      String lastDir = Settings.getInstance().getLastWorkspaceDir();
      WbFileChooser fc = new WbFileChooser(lastDir);

      FileFilter wkspFF = ExtensionFileFilter.getWorkspaceFileFilter();
      fc.removeChoosableFileFilter(fc.getFileFilter()); // remove the default "All files" filter
      fc.addChoosableFileFilter(wkspFF);
      fc.setMultiSelectionEnabled(false);
      fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

      String filename = null;

      if (toSave)
      {
        fc.setDialogTitle(ResourceMgr.getString("TxtSaveWksp"));
      }
      else
      {
        fc.setDialogTitle(ResourceMgr.getString("TxtLoadWksp"));
      }

      int answer = JFileChooser.CANCEL_OPTION;

      boolean done = false;
      while (!done)
      {
        if (toSave)
        {
          answer = fc.showSaveDialog(parentWindow);
        }
        else
        {
          answer = fc.showOpenDialog(parentWindow);
        }

        done = true;

        if (answer == JFileChooser.APPROVE_OPTION)
        {
          File selected = fc.getSelectedFile();
          if (!isValidWorkspace(selected))
          {
            YesNoCancel choice = confirmDirectorySelection(selected);
            if (choice == YesNoCancel.cancel)
            {
              filename = null;
              break;
            }

            if (choice == YesNoCancel.no)
            {
              done = false;
              continue;
            }
          }

          FileFilter ff = fc.getFileFilter();
          if (ff == wkspFF)
          {
            filename = selected.getAbsolutePath();

            String ext = ExtensionFileFilter.getExtension(selected);
            if (StringUtil.isEmpty(ext) && !selected.isDirectory())
            {
              if (!filename.endsWith(".")) filename += ".";
              filename += ExtensionFileFilter.WORKSPACE_EXT;
            }
          }
          else
          {
            filename = selected.getAbsolutePath();
          }
          lastDir = selected.getParentFile().getAbsolutePath();
          Settings.getInstance().setLastWorkspaceDir(lastDir);
        }
      }

      if (filename != null && Settings.getInstance().shortenWorkspaceFileName())
      {
        filename = shortenFilename(filename);
      }

      return filename;
    }
    catch (Exception e)
    {
      LogMgr.logError(new CallerInfo(){}, "Error selecting file", e);
      WbSwingUtilities.showErrorMessage(ExceptionUtil.getDisplay(e));
      return null;
    }
  }

  public static String shortenFilename(String filename)
  {
    File f = new File(filename);
    filename = FileDialogUtil.removeWorkspaceDir(filename);
    if (f.isDirectory() && PlatformHelper.isWindows())
    {
      filename = filename.replace("\\", "/");
    }
    return filename;
  }

  private YesNoCancel confirmDirectorySelection(File selected)
  {
    String msg = ResourceMgr.getFormattedString("MsgWkspBckDirNotEmpty", selected.getAbsolutePath());
    return WbSwingUtilities.getYesNoCancel(parentWindow, msg);
  }

  private boolean isValidWorkspace(File selected)
  {
    if (selected == null) return false;
    if (selected.isFile() || !selected.exists())
    {
      return true;
    }

    // Only allow directories that are empty or already contain a workspace
    File props = new File(selected, WbWorkspace.TABINFO_FILENAME);
    if (props.exists())
    {
      return true;
    }
    File[] files = selected.listFiles();
    return (files == null || files.length == 0);
  }
}