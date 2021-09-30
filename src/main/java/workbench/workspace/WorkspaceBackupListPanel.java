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
package workbench.workspace;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import workbench.log.CallerInfo;
import workbench.log.LogMgr;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

import workbench.gui.components.WbSplitPane;
import workbench.gui.components.WbTable;
import workbench.gui.sql.PanelType;
import workbench.gui.sql.SqlEditor;
import workbench.gui.sql.SqlHistory;
import workbench.gui.sql.SqlHistoryEntry;

import workbench.util.FileDialogUtil;

/**
 * A class to show the backups for a specific workspace including a summary of the content.
 *
 * @author Thomas Kellerer
 */
public class WorkspaceBackupListPanel
  extends JPanel
  implements ListSelectionListener, ActionListener
{

  private File workspaceFile;
  private FileListTableModel backups;
  private SqlEditor editor;
  private WbTable filesTable;
  private JList<TabEntry> tabList;
  private JButton selectWorkspaceButton;

  public WorkspaceBackupListPanel(File workspace)
  {
    this.workspaceFile = workspace;
    initComponents();

    String fname = workspaceFile.getName();
    String dir = Settings.getInstance().getBackupDir();
    workspaceFileName.setText(ResourceMgr.getFormattedString("LblWkspBackups", fname, dir));

    filesTable = new WbTable(false, false, false);
    filesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    filesScrollPane.setViewportView(filesTable);
    tabList = new JList();
    tabsScrollPane.setViewportView(tabList);

    int height = filesTable.getRowHeight();
    splitPane.setDividerLocation(height * 15);

    editor = new SqlEditor();
    editor.setEditable(false);
    splitPane.setRightComponent(editor);
    filesTable.getSelectionModel().addListSelectionListener(this);
    listSplitPane.setDividerLocation(0.5);
    EventQueue.invokeLater(() -> {loadBackups();});
  }

  private void loadBackups()
  {
    resetTabList();
    WorkspaceBackupList list = new WorkspaceBackupList(workspaceFile);
    List<File> files = list.getBackups();
    backups = new FileListTableModel(files);
    filesTable.setModel(backups);
  }
  public void showSelectButton()
  {
    if (selectWorkspaceButton != null) return;
    selectWorkspaceButton = new JButton("...");
    selectWorkspaceButton.addActionListener(this);
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 1;
    gc.gridy = 0;
    gc.gridwidth = 1;
    gc.gridheight = 1;
    gc.weightx = 0.0;
    gc.fill = GridBagConstraints.NONE;
    gc.anchor = GridBagConstraints.LINE_END;
    headerPanel.add(selectWorkspaceButton, gc);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == selectWorkspaceButton)
    {
      FileDialogUtil util = new FileDialogUtil();
      String filename = util.getWorkspaceFilename(SwingUtilities.getWindowAncestor(this), false, true);
      if (filename != null)
      {
        this.workspaceFile = new File(filename);
        loadBackups();
      }
    }
  }

  @Override
  public void valueChanged(ListSelectionEvent e)
  {
    if (e.getValueIsAdjusting()) return;
    if (e.getSource() == filesTable.getSelectionModel())
    {
      int index = filesTable.getSelectedRow();
      if (index > -1 && index < backups.getRowCount())
      {
        resetTabList();
        loadWorkspace();
      }
    }
    else if (e.getSource() == tabList.getSelectionModel())
    {
      loadEditorContent(tabList.getSelectedIndex());
    }
  }

  public File getSelectedWorkspaceFile()
  {
    int index = filesTable.getSelectedRow();
    if (index < -1 || index > backups.getRowCount()) return null;
    return backups.getFile(index);
  }

  private WbWorkspace getSelectedWorkspace()
  {
    int index = filesTable.getSelectedRow();
    if (index < -1 || index > backups.getRowCount()) return null;
    File f = backups.getFile(index);
    return new WbWorkspace(f.getAbsolutePath());
  }

  private void loadWorkspace()
  {
    WbWorkspace wksp = getSelectedWorkspace();
    if (wksp == null) return;
    try
    {
      wksp.openForReading();
      int tabCount = wksp.getEntryCount();
      DefaultListModel<TabEntry> tabNames = new DefaultListModel<>();
      for (int i=0; i < tabCount; i++)
      {
        PanelType type = wksp.getPanelType(i);
        if (type == PanelType.sqlPanel)
        {
          TabEntry entry = new TabEntry(i, wksp.getTabTitle(i));
          tabNames.addElement(entry);
        }
      }
      tabList.setModel(tabNames);
      editor.setText("");
    }
    catch (Exception io)
    {
      LogMgr.logError(new CallerInfo(){}, "Could not load workspace", io);
    }
    finally
    {
      wksp.close();
    }
  }

  private void resetTabList()
  {
    tabList.getSelectionModel().removeListSelectionListener(this);
    tabList.setModel(new DefaultListModel<>());
    editor.setText("");
    tabList.getSelectionModel().addListSelectionListener(this);
  }

  private void loadEditorContent(int index)
  {
    TabEntry entry = tabList.getModel().getElementAt(index);
    WbWorkspace wksp = getSelectedWorkspace();
    if (wksp == null) return;
    try
    {
      wksp.openForReading();
      SqlHistory history = new SqlHistory(editor, 10);
      wksp.readHistoryData(entry.getWorkspaceIndex(), history);
      SqlHistoryEntry content = history.getTopEntry();
      editor.setText(content.getText());
      editor.setCaretPosition(0);
      editor.invalidate();
    }
    catch (Exception ex)
    {
      LogMgr.logError(new CallerInfo(){}, "Could not load editor content", ex);
    }
    finally
    {
      wksp.close();
    }
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {
    java.awt.GridBagConstraints gridBagConstraints;

    splitPane = new WbSplitPane();
    listSplitPane = new javax.swing.JSplitPane();
    filesScrollPane = new javax.swing.JScrollPane();
    tabsScrollPane = new javax.swing.JScrollPane();
    headerPanel = new javax.swing.JPanel();
    workspaceFileName = new javax.swing.JLabel();

    setLayout(new java.awt.GridBagLayout());

    splitPane.setDividerLocation(200);
    splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

    listSplitPane.setLeftComponent(filesScrollPane);
    listSplitPane.setRightComponent(tabsScrollPane);

    splitPane.setLeftComponent(listSplitPane);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(splitPane, gridBagConstraints);

    headerPanel.setMinimumSize(new java.awt.Dimension(488, 50));
    headerPanel.setLayout(new java.awt.GridBagLayout());

    workspaceFileName.setText("jLabel1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    headerPanel.add(workspaceFileName, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 10, 0);
    add(headerPanel, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JScrollPane filesScrollPane;
  private javax.swing.JPanel headerPanel;
  private javax.swing.JSplitPane listSplitPane;
  private javax.swing.JSplitPane splitPane;
  private javax.swing.JScrollPane tabsScrollPane;
  private javax.swing.JLabel workspaceFileName;
  // End of variables declaration//GEN-END:variables
}