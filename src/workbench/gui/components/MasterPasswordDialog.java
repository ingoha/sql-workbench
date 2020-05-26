/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2020 Thomas Kellerer.
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
package workbench.gui.components;

import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JPasswordField;

import workbench.resource.IconMgr;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

import workbench.gui.WbSwingUtilities;
import workbench.gui.actions.EscAction;

import com.sun.org.apache.xpath.internal.operations.Mod;

/**
 *
 * @author Thomas Kellerer
 */
public class MasterPasswordDialog
  extends JDialog
  implements ActionListener
{
  private boolean cancelled = true;
  private boolean removeMasterPwd = false;
  private EscAction escAction;
  private char echoChar;

  public MasterPasswordDialog(Frame parent, boolean allowRemove)
  {
    super(parent, ResourceMgr.getString("TxtWindowTitleMasterPwd"), true);
    init(allowRemove);
  }
  public MasterPasswordDialog(Dialog parent)
  {
    super(parent, ResourceMgr.getString("TxtWindowTitleMasterPwd"), true);
    init(false);
  }

  private void init(boolean allowRemove)
  {
    escAction = new EscAction(this, this);

    setResizable(true);
    initComponents();
    warningLabel.setIconTextGap(IconMgr.getInstance().getSizeForLabel() / 2);
    warningLabel.setIcon(IconMgr.getInstance().getLabelIcon("alert"));
    warningLabel.setFont(warningLabel.getFont().deriveFont(Font.BOLD));
    pack();
    if (!allowRemove || !Settings.getInstance().getUseMasterPassword())
    {
      removePwdButton.setVisible(false);
    }
    echoChar = pwdInput.getEchoChar();
    showPwd.setIcon(IconMgr.getInstance().getLabelIcon("eye"));
    showRepeat.setIcon(IconMgr.getInstance().getLabelIcon("eye"));
    showPwd.setMargin(pwdInput.getMargin());
    WbSwingUtilities.makeEqualHeight(pwdInput, showPwd);
    showRepeat.setMargin(pwdInput.getMargin());
    WbSwingUtilities.makeEqualHeight(repeatPwd, showRepeat);
  }

  public boolean doRemoveMasterPassword()
  {
    return removeMasterPwd;
  }

  public boolean wasCancelled()
  {
    return cancelled;
  }

  public String getMasterPassword()
  {
    if (removeMasterPwd) return null;
    return pwdInput.getText();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == escAction)
    {
      doCancel();
    }
  }

  private void doCancel()
  {
    this.removeMasterPwd = false;
    this.cancelled = true;
    closeWindow();
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

    oldPwdLabel = new javax.swing.JLabel();
    newPwdLabel = new javax.swing.JLabel();
    pwdInput = new javax.swing.JPasswordField();
    repeatPwd = new javax.swing.JPasswordField();
    jPanel1 = new javax.swing.JPanel();
    removePwdButton = new javax.swing.JButton();
    okButton = new WbButton();
    cancelButton = new WbButton();
    jPanel2 = new javax.swing.JPanel();
    warningLabel = new javax.swing.JLabel();
    showPwd = new FlatButton();
    showRepeat = new FlatButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    getContentPane().setLayout(new java.awt.GridBagLayout());

    oldPwdLabel.setText(ResourceMgr.getString("LblNewPwd")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(13, 12, 8, 0);
    getContentPane().add(oldPwdLabel, gridBagConstraints);

    newPwdLabel.setText(ResourceMgr.getString("LblPwdRepeat")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(0, 12, 8, 0);
    getContentPane().add(newPwdLabel, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(13, 8, 8, 0);
    getContentPane().add(pwdInput, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 8, 8, 0);
    getContentPane().add(repeatPwd, gridBagConstraints);

    jPanel1.setLayout(new java.awt.GridBagLayout());

    removePwdButton.setText("Remove");
    removePwdButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        removePwdButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(removePwdButton, gridBagConstraints);

    okButton.setText(ResourceMgr.getString("LblOK")); // NOI18N
    okButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        okButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
    jPanel1.add(okButton, gridBagConstraints);

    cancelButton.setText(ResourceMgr.getString("LblCancel")); // NOI18N
    cancelButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        cancelButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
    gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
    jPanel1.add(cancelButton, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 1.0;
    jPanel1.add(jPanel2, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(19, 12, 16, 12);
    getContentPane().add(jPanel1, gridBagConstraints);

    warningLabel.setText(ResourceMgr.getString("LblMasterPwdWarn")); // NOI18N
    warningLabel.setIconTextGap(8);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(5, 12, 0, 12);
    getContentPane().add(warningLabel, gridBagConstraints);

    showPwd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/workbench/resource/images/eye16.png"))); // NOI18N
    showPwd.setIconTextGap(0);
    showPwd.setMargin(new java.awt.Insets(0, 0, 0, 0));
    showPwd.setMaximumSize(null);
    showPwd.setMinimumSize(null);
    showPwd.setPreferredSize(null);
    showPwd.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        showPwdActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 13);
    getContentPane().add(showPwd, gridBagConstraints);

    showRepeat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/workbench/resource/images/eye16.png"))); // NOI18N
    showRepeat.setIconTextGap(0);
    showRepeat.setMargin(new java.awt.Insets(0, 0, 0, 0));
    showRepeat.setMaximumSize(null);
    showRepeat.setMinimumSize(null);
    showRepeat.setPreferredSize(null);
    showRepeat.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        showRepeatActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 13);
    getContentPane().add(showRepeat, gridBagConstraints);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_cancelButtonActionPerformed
  {//GEN-HEADEREND:event_cancelButtonActionPerformed
    doCancel();
  }//GEN-LAST:event_cancelButtonActionPerformed

  private void okButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_okButtonActionPerformed
  {//GEN-HEADEREND:event_okButtonActionPerformed
    char[] pwd = pwdInput.getPassword();
    if (pwd == null || pwd.length == 0)
    {
      WbSwingUtilities.showErrorMessageKey(this, "MsgNoPwd");
      return;
    }
    if (!Arrays.equals(pwdInput.getPassword(), repeatPwd.getPassword()))
    {
      WbSwingUtilities.showErrorMessageKey(this, "MsgPwdNoMatch");
      return;
    }
    this.removeMasterPwd = false;
    this.cancelled = false;
    closeWindow();
  }//GEN-LAST:event_okButtonActionPerformed

  private void removePwdButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_removePwdButtonActionPerformed
  {//GEN-HEADEREND:event_removePwdButtonActionPerformed
    this.removeMasterPwd = true;
    this.cancelled = false;
    closeWindow();
  }//GEN-LAST:event_removePwdButtonActionPerformed

  private void showPwdActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showPwdActionPerformed
  {//GEN-HEADEREND:event_showPwdActionPerformed
    togglePasswordVisible(pwdInput);
  }//GEN-LAST:event_showPwdActionPerformed

  private void showRepeatActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_showRepeatActionPerformed
  {//GEN-HEADEREND:event_showRepeatActionPerformed
    togglePasswordVisible(repeatPwd);
  }//GEN-LAST:event_showRepeatActionPerformed

  private void togglePasswordVisible(JPasswordField field)
  {
    if (field.getEchoChar() == (char)0)
    {
      field.setEchoChar(echoChar);
    }
    else
    {
      field.setEchoChar((char)0);
    }
  }

  private void closeWindow()
  {
    this.dispose();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelButton;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JLabel newPwdLabel;
  private javax.swing.JButton okButton;
  private javax.swing.JLabel oldPwdLabel;
  private javax.swing.JPasswordField pwdInput;
  private javax.swing.JButton removePwdButton;
  private javax.swing.JPasswordField repeatPwd;
  private javax.swing.JButton showPwd;
  private javax.swing.JButton showRepeat;
  private javax.swing.JLabel warningLabel;
  // End of variables declaration//GEN-END:variables
}
