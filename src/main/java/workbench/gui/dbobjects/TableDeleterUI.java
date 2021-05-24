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
package workbench.gui.dbobjects;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;

import workbench.interfaces.JobErrorHandler;
import workbench.interfaces.StatusBar;
import workbench.interfaces.TableDeleteListener;
import workbench.log.CallerInfo;
import workbench.log.LogMgr;
import workbench.resource.ResourceMgr;

import workbench.db.CommitType;
import workbench.db.TableDeleter;
import workbench.db.TableIdentifier;
import workbench.db.WbConnection;
import workbench.db.importer.TableDependencySorter;

import workbench.gui.WbSwingUtilities;
import workbench.gui.components.EditWindow;
import workbench.gui.components.NoSelectionModel;
import workbench.gui.components.WbButton;
import workbench.gui.components.WbStatusLabel;

import workbench.util.CollectionUtil;
import workbench.util.ExceptionUtil;
import workbench.util.WbThread;

/**
 *
 * @author  Thomas Kellerer
 */
public class TableDeleterUI
  extends javax.swing.JPanel
  implements WindowListener, JobErrorHandler
{
  private JDialog dialog;
  private List<TableIdentifier> objectNames;
  private boolean cancelled;
  private WbConnection connection;
  private Thread deleteThread;
  private Thread checkThread;
  private final List<TableDeleteListener> deleteListener = new ArrayList<>(1);
  private TableDeleter deleter;

  public TableDeleterUI()
  {
    super();
    initComponents();
    statusLabel.setText("  ");
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {
    GridBagConstraints gridBagConstraints;

    buttonGroup1 = new ButtonGroup();
    buttonPanel = new JPanel();
    deleteButton = new WbButton();
    cancelButton = new WbButton();
    showScript = new JButton();
    mainPanel = new JPanel();
    jScrollPane1 = new JScrollPane();
    objectList = new JList();
    optionPanel = new JPanel();
    statusLabel = new WbStatusLabel();
    jPanel2 = new JPanel();
    commitEach = new JRadioButton();
    commitAtEnd = new JRadioButton();
    useTruncateCheckBox = new JCheckBox();
    cascadeTruncate = new JCheckBox();
    jSeparator1 = new JSeparator();
    addMissingTables = new JCheckBox();
    checkFKButton = new JButton();

    setLayout(new BorderLayout(0, 5));

    buttonPanel.setLayout(new GridBagLayout());

    deleteButton.setText(ResourceMgr.getString("LblDeleteTableData")); // NOI18N
    deleteButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        deleteButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 20, 0, 20);
    buttonPanel.add(deleteButton, gridBagConstraints);

    cancelButton.setText(ResourceMgr.getString("LblClose")); // NOI18N
    cancelButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        cancelButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = GridBagConstraints.EAST;
    gridBagConstraints.insets = new Insets(0, 20, 0, 2);
    buttonPanel.add(cancelButton, gridBagConstraints);

    showScript.setText(ResourceMgr.getString("LblShowScript")); // NOI18N
    showScript.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        showScriptActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(1, 1, 0, 20);
    buttonPanel.add(showScript, gridBagConstraints);

    add(buttonPanel, BorderLayout.SOUTH);

    mainPanel.setLayout(new BorderLayout(0, 5));

    objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    objectList.setSelectionModel(new NoSelectionModel());
    jScrollPane1.setViewportView(objectList);

    mainPanel.add(jScrollPane1, BorderLayout.CENTER);

    optionPanel.setLayout(new GridBagLayout());

    statusLabel.setText("  ");
    statusLabel.setBorder(BorderFactory.createEtchedBorder());
    statusLabel.setMinimumSize(new Dimension(150, 24));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(6, 0, 0, 0);
    optionPanel.add(statusLabel, gridBagConstraints);

    jPanel2.setLayout(new GridBagLayout());

    buttonGroup1.add(commitEach);
    commitEach.setSelected(true);
    commitEach.setText(ResourceMgr.getString("LblCommitEachTableDelete")); // NOI18N
    commitEach.setBorder(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(0, 6, 0, 0);
    jPanel2.add(commitEach, gridBagConstraints);

    buttonGroup1.add(commitAtEnd);
    commitAtEnd.setText(ResourceMgr.getString("LblCommitTableDeleteAtEnd")); // NOI18N
    commitAtEnd.setBorder(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(4, 6, 0, 0);
    jPanel2.add(commitAtEnd, gridBagConstraints);

    useTruncateCheckBox.setText(ResourceMgr.getString("LblUseTruncate")); // NOI18N
    useTruncateCheckBox.setBorder(null);
    useTruncateCheckBox.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent evt)
      {
        useTruncateCheckBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 25, 0, 0);
    jPanel2.add(useTruncateCheckBox, gridBagConstraints);

    cascadeTruncate.setText(ResourceMgr.getString("LblCascadeConstraints")); // NOI18N
    cascadeTruncate.setBorder(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(3, 41, 0, 0);
    jPanel2.add(cascadeTruncate, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(0, 4, 0, 9);
    optionPanel.add(jPanel2, gridBagConstraints);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new Insets(8, 0, 12, 0);
    optionPanel.add(jSeparator1, gridBagConstraints);

    addMissingTables.setSelected(true);
    addMissingTables.setText(ResourceMgr.getString("LblIncFkTables")); // NOI18N
    addMissingTables.setToolTipText(ResourceMgr.getString("d_LblIncFkTables")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(8, 2, 0, 0);
    optionPanel.add(addMissingTables, gridBagConstraints);

    checkFKButton.setText(ResourceMgr.getString("LblCheckFKDeps")); // NOI18N
    checkFKButton.setToolTipText(ResourceMgr.getDescription("LblCheckFKDeps"));
    checkFKButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        checkFKButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(8, 6, 0, 5);
    optionPanel.add(checkFKButton, gridBagConstraints);

    mainPanel.add(optionPanel, BorderLayout.SOUTH);

    add(mainPanel, BorderLayout.CENTER);
  }// </editor-fold>//GEN-END:initComponents

  private void useTruncateCheckBoxItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_useTruncateCheckBoxItemStateChanged
  {//GEN-HEADEREND:event_useTruncateCheckBoxItemStateChanged
    checkState();
  }//GEN-LAST:event_useTruncateCheckBoxItemStateChanged

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
  {//GEN-HEADEREND:event_cancelButtonActionPerformed
    if (this.deleter != null)
    {
      this.deleter.cancel();
    }
    this.cancelled = true;
    closeWindow();
  }//GEN-LAST:event_cancelButtonActionPerformed

  private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_deleteButtonActionPerformed
  {//GEN-HEADEREND:event_deleteButtonActionPerformed
    this.startDelete();
  }//GEN-LAST:event_deleteButtonActionPerformed

  private void checkFKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkFKButtonActionPerformed

    if (this.connection.isBusy())
    {
      return;
    }

    this.deleteButton.setEnabled(false);
    this.showScript.setEnabled(false);
    this.statusLabel.setText(ResourceMgr.getString("MsgFkDeps"));

    WbSwingUtilities.showWaitCursor(dialog);

    this.checkThread = new WbThread("FKCheck")
    {

      @Override
      public void run()
      {
        List<TableIdentifier> sorted = null;
        try
        {
          connection.setBusy(true);
          TableDependencySorter sorter = new TableDependencySorter(connection);
          sorter.setValidateTables(false);
          sorted = sorter.sortForDelete(objectNames, addMissingTables.isSelected());
        }
        catch (Exception e)
        {
          LogMgr.logError(new CallerInfo(){}, "Error checking FK dependencies", e);
          WbSwingUtilities.showErrorMessage(ExceptionUtil.getDisplay(e));
          sorted = null;
        }
        finally
        {
          connection.setBusy(false);
          fkCheckFinished(sorted);
        }
      }
    };

    checkThread.start();
  }//GEN-LAST:event_checkFKButtonActionPerformed

  private void showScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showScriptActionPerformed
    showScript();
  }//GEN-LAST:event_showScriptActionPerformed

  protected void fkCheckFinished(final List<TableIdentifier> newlist)
  {
    this.checkThread = null;
    EventQueue.invokeLater(() ->
    {
      statusLabel.setText("");
      if (newlist != null)
      {
        setObjects(newlist);
      }
      deleteButton.setEnabled(true);
      showScript.setEnabled(true);
      WbSwingUtilities.showDefaultCursor(dialog);
    });
  }

  @Override
  public void fatalError(String msg)
  {
    WbSwingUtilities.showErrorMessage(this, msg);
  }

  @Override
  public int getActionOnError(int errorRow, String errorColumn, String data, String errorMessage)
  {
    int choice = WbSwingUtilities.getYesNoIgnoreAll(this.dialog, errorMessage);
    if (choice == WbSwingUtilities.IGNORE_ALL)
    {
      return JobErrorHandler.JOB_IGNORE_ALL;
    }
    if (choice == JOptionPane.YES_OPTION)
    {
      return JobErrorHandler.JOB_CONTINUE;
    }
    return JobErrorHandler.JOB_ABORT;
  }

  protected void closeWindow()
  {
    try
    {
      if (this.deleter != null)
      {
        this.deleter.cancel();
      }
      if (this.deleteThread != null)
      {
        this.deleteThread.interrupt();
        this.deleteThread = null;
      }
    }
    catch (Exception e)
    {
      LogMgr.logWarning(new CallerInfo(){}, "Error when trying to kill delete Thread", e);
    }

    try
    {
      if (this.checkThread != null)
      {
        this.checkThread.interrupt();
        this.checkThread = null;
      }
    }
    catch (Exception e)
    {
      LogMgr.logWarning(new CallerInfo(){}, "Error when trying to kill check thread", e);
    }

    try
    {
      if (this.dialog != null)
      {
        this.dialog.setVisible(false);
      }
    }
    catch (Throwable th)
    {
      // ignore
    }
    this.dialog = null;
  }

  public void setConnection(WbConnection aConn)
  {
    this.connection = aConn;
    if (connection != null)
    {
      useTruncateCheckBox.setEnabled(this.connection.getDbSettings().supportsTruncate());
      checkState();
    }
  }

  protected void checkState()
  {
    boolean autoCommit = connection == null ? true : connection.getAutoCommit();
    boolean useTruncate = useTruncateCheckBox.isSelected() && useTruncateCheckBox.isEnabled();
    if (autoCommit)
    {
      commitAtEnd.setEnabled(false);
      commitEach.setEnabled(false);
    }
    else if (useTruncate)
    {
      commitAtEnd.setEnabled(connection.getDbSettings().truncateNeedsCommit());
      commitEach.setEnabled(connection.getDbSettings().truncateNeedsCommit());
    }
    else
    {
      commitAtEnd.setEnabled(true);
      commitEach.setEnabled(true);
    }

    boolean canCascade = connection == null ? false : connection.getDbSettings().supportsCascadedTruncate();
    cascadeTruncate.setEnabled(canCascade && useTruncate);
    if (!canCascade)
    {
      cascadeTruncate.setSelected(false);
    }
  }

  protected void startDelete()
  {
    deleteButton.setEnabled(false);

    this.deleteThread = new WbThread("TableDeleteThread")
    {
      @Override
      public void run()
      {
        doDelete();
      }
    };
    this.deleteThread.start();
  }

  protected void doDelete()
  {
    this.cancelled = false;

    boolean doCommitEach = commitEach.isEnabled() && this.commitEach.isSelected();
    boolean useTruncate = useTruncateCheckBox.isSelected();
    boolean cascadedTruncate = useTruncate ? cascadeTruncate.isSelected() : false;

    deleter = new TableDeleter(this.connection);
    deleter.setStatusBar((StatusBar)statusLabel);

    boolean hasError = false;
    List<TableIdentifier> deletedTables = null;

    try
    {
      cancelButton.setText(ResourceMgr.getString("LblCancel"));
      deletedTables = deleter.deleteTableData(this.objectNames, doCommitEach, useTruncate, cascadedTruncate);
    }
    catch (SQLException e)
    {
      // Basically any error should have been handled by the TableDeleter
      // or through the JobErrorHandler callbacks
      WbSwingUtilities.showErrorMessage(this, ExceptionUtil.getDisplay(e));
    }
    finally
    {
      cancelButton.setText(ResourceMgr.getString("LblClose"));
      EventQueue.invokeLater(() ->
      {
        deleteButton.setEnabled(true);
      });
    }

    this.fireTableDeleted(deletedTables);

    this.statusLabel.setText("");
    this.deleter = null;

    if (!hasError)
    {
      this.closeWindow();
    }
  }

  protected void showScript()
  {
    CommitType commit = CommitType.once;
    if (this.commitEach.isSelected())
    {
      commit = CommitType.each;
    }
    boolean useTruncate = this.useTruncateCheckBox.isSelected();
    boolean cascade = useTruncate && cascadeTruncate.isSelected();
    TableDeleter tblDeleter = new TableDeleter(this.connection);
    CharSequence script = tblDeleter.generateScript(objectNames, commit, useTruncate, cascade);
    final EditWindow w = new EditWindow(this.dialog, ResourceMgr.getString("TxtWindowTitleGeneratedScript"), script.toString(), "workbench.tabledeleter.scriptwindow", true);
    w.setVisible(true);
    w.dispose();
  }

  public boolean dialogWasCancelled()
  {
    return this.cancelled;
  }

  public void setObjects(List<TableIdentifier> objects)
  {
    this.objectNames = objects == null ? CollectionUtil.arrayList() : new ArrayList<>(objects);
    int numNames = this.objectNames.size();

    String[] display = new String[numNames];
    for (int i = 0; i < numNames; i++)
    {
      display[i] = this.objectNames.get(i).getObjectExpression(connection);
    }
    this.objectList.setListData(display);
  }

  public void showDialog(Frame aParent)
  {
    this.dialog = new JDialog(aParent, ResourceMgr.getString("TxtDeleteTableData"), false);
    this.dialog.getContentPane().add(this);
    this.dialog.pack();
    if (this.dialog.getWidth() < 200)
    {
      this.dialog.setSize(200, this.dialog.getHeight());
    }
    WbSwingUtilities.center(this.dialog, aParent);
    this.cancelled = true;
    this.dialog.setVisible(true);
  }

  public void addDeleteListener(TableDeleteListener listener)
  {
    if (listener != null)
    {
      this.deleteListener.add(listener);
    }
  }

  public void removeDeleteListener(TableDeleteListener listener)
  {
    if (listener != null)
    {
      this.deleteListener.remove(listener);
    }
  }

  protected void fireTableDeleted(List<TableIdentifier> tables)
  {
    if (this.deleteListener == null || tables == null)
    {
      return;
    }

    for (TableDeleteListener l : this.deleteListener)
    {
      if (l != null) l.tableDataDeleted(tables);
    }
  }

  @Override
  public void windowActivated(WindowEvent e)
  {
  }

  @Override
  public void windowClosed(WindowEvent e)
  {
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    this.cancelled = true;
    closeWindow();
  }

  @Override
  public void windowDeactivated(WindowEvent e)
  {
  }

  @Override
  public void windowDeiconified(WindowEvent e)
  {
  }

  @Override
  public void windowIconified(WindowEvent e)
  {
  }

  @Override
  public void windowOpened(WindowEvent e)
  {
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public JCheckBox addMissingTables;
  public ButtonGroup buttonGroup1;
  public JPanel buttonPanel;
  public JButton cancelButton;
  public JCheckBox cascadeTruncate;
  public JButton checkFKButton;
  public JRadioButton commitAtEnd;
  public JRadioButton commitEach;
  public JButton deleteButton;
  public JPanel jPanel2;
  public JScrollPane jScrollPane1;
  public JSeparator jSeparator1;
  public JPanel mainPanel;
  public JList objectList;
  public JPanel optionPanel;
  public JButton showScript;
  public JLabel statusLabel;
  public JCheckBox useTruncateCheckBox;
  // End of variables declaration//GEN-END:variables
}
