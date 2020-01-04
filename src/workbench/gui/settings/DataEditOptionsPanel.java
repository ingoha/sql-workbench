/*
 * DataEditOptionsPanel.java
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
package workbench.gui.settings;

import javax.swing.JPanel;

import workbench.interfaces.Restoreable;
import workbench.resource.GuiSettings;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

import workbench.gui.components.FlatButton;

import workbench.util.FileDialogUtil;
import workbench.util.StringUtil;

/**
 * A panel to edit the options for data editing, such as the font to be
 * used in the JTable, the PK Mapping file, colors used for required
 * fields and alternating row coloring.
 *
 * @author  Thomas Kellerer
 */
public class DataEditOptionsPanel
  extends JPanel
  implements java.awt.event.ActionListener, Restoreable
{

  public DataEditOptionsPanel()
  {
    super();
    initComponents();
  }

  @Override
  public void restoreSettings()
  {
    pkMapFile.setCaretPosition(0);
    previewDml.setSelected(Settings.getInstance().getPreviewDml());
    requiredFieldColor.setSelectedColor(GuiSettings.getRequiredFieldColor());
    highlightRequired.setSelected(GuiSettings.getHighlightRequiredFields());
    pkMapFile.setText(Settings.getInstance().getPKMappingFilename());
    warnDiscard.setSelected(GuiSettings.getConfirmDiscardResultSetChanges());
    fieldLength.setText(Integer.toString(GuiSettings.getDefaultFormFieldWidth()));
    inputLines.setText(Integer.toString(GuiSettings.getDefaultFormFieldLines()));
    checkEditable.setSelected(Settings.getInstance().getCheckEditableColumns());
  }

  @Override
  public void saveSettings()
  {
    GuiSettings.setRequiredFieldColor(requiredFieldColor.getSelectedColor());
    GuiSettings.setHighlightRequiredFields(this.highlightRequired.isSelected());
    GuiSettings.setConfirmDiscardResultSetChanges(warnDiscard.isSelected());
    Settings.getInstance().setPreviewDml(this.previewDml.isSelected());
    Settings.getInstance().setPKMappingFilename(pkMapFile.getText());

    int chars = StringUtil.getIntValue(fieldLength.getText(), -1);
    if (chars > 0)
    {
      GuiSettings.setDefaultFormFieldWidth(chars);
    }
    int lines = StringUtil.getIntValue(inputLines.getText(), -1);
    if (chars > 0)
    {
      GuiSettings.setDefaultFormFieldLines(lines);
    }
    Settings.getInstance().setCheckEditableColumns(checkEditable.isSelected());
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {
    java.awt.GridBagConstraints gridBagConstraints;

    previewDml = new javax.swing.JCheckBox();
    warnDiscard = new javax.swing.JCheckBox();
    checkEditable = new javax.swing.JCheckBox();
    requiredFieldColor = new workbench.gui.components.WbColorPicker();
    highlightRequired = new javax.swing.JCheckBox();
    jPanel1 = new javax.swing.JPanel();
    pkMapFileLabel = new javax.swing.JLabel();
    pkMapFile = new javax.swing.JTextField();
    selectMapFile = new FlatButton();
    jPanel3 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    fieldLength = new javax.swing.JTextField();
    inputLines = new javax.swing.JTextField();

    setLayout(new java.awt.GridBagLayout());

    previewDml.setText(ResourceMgr.getString("LblPreviewDml")); // NOI18N
    previewDml.setToolTipText(ResourceMgr.getString("d_LblPreviewDml")); // NOI18N
    previewDml.setBorder(null);
    previewDml.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    previewDml.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    previewDml.setIconTextGap(5);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 12);
    add(previewDml, gridBagConstraints);

    warnDiscard.setText(ResourceMgr.getString("LblWarnChgResultSet")); // NOI18N
    warnDiscard.setToolTipText(ResourceMgr.getString("d_LblWarnChgResultSet")); // NOI18N
    warnDiscard.setBorder(null);
    warnDiscard.setMargin(new java.awt.Insets(0, 0, 0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 1);
    add(warnDiscard, gridBagConstraints);

    checkEditable.setText(ResourceMgr.getString("LblCheckEditable")); // NOI18N
    checkEditable.setToolTipText(ResourceMgr.getString("d_LblCheckEditable")); // NOI18N
    checkEditable.setBorder(null);
    checkEditable.setMargin(new java.awt.Insets(0, 0, 0, 0));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 1);
    add(checkEditable, gridBagConstraints);

    requiredFieldColor.setToolTipText(ResourceMgr.getString("LblReqFldColor"));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 25);
    add(requiredFieldColor, gridBagConstraints);

    highlightRequired.setText(ResourceMgr.getString("LblHiliteRqd")); // NOI18N
    highlightRequired.setToolTipText(ResourceMgr.getString("d_LblHiliteRqd")); // NOI18N
    highlightRequired.setBorder(null);
    highlightRequired.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    highlightRequired.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    highlightRequired.setIconTextGap(5);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 1);
    add(highlightRequired, gridBagConstraints);

    jPanel1.setLayout(new java.awt.GridBagLayout());

    pkMapFileLabel.setText(ResourceMgr.getString("LblPKMapFile")); // NOI18N
    pkMapFileLabel.setToolTipText(ResourceMgr.getString("d_LblPKMapFile")); // NOI18N
    pkMapFileLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 10));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    jPanel1.add(pkMapFileLabel, gridBagConstraints);

    pkMapFile.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    jPanel1.add(pkMapFile, gridBagConstraints);

    selectMapFile.setText("...");
    selectMapFile.setMinimumSize(new java.awt.Dimension(22, 22));
    selectMapFile.setPreferredSize(new java.awt.Dimension(22, 22));
    selectMapFile.addActionListener(this);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
    jPanel1.add(selectMapFile, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 16);
    add(jPanel1, gridBagConstraints);

    jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(ResourceMgr.getString("LblFreeFormSettings"))); // NOI18N
    jPanel3.setLayout(new java.awt.GridBagLayout());

    jLabel1.setText(ResourceMgr.getString("LblFreeFormFieldLen")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 0);
    jPanel3.add(jLabel1, gridBagConstraints);

    jLabel2.setText(ResourceMgr.getString("LblFreeFormLines")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(8, 5, 6, 0);
    jPanel3.add(jLabel2, gridBagConstraints);

    fieldLength.setColumns(5);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(6, 9, 0, 8);
    jPanel3.add(fieldLength, gridBagConstraints);

    inputLines.setColumns(5);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(9, 9, 6, 8);
    jPanel3.add(inputLines, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 10);
    add(jPanel3, gridBagConstraints);
  }

  // Code for dispatching events from components to event handlers.

  public void actionPerformed(java.awt.event.ActionEvent evt)
  {
    if (evt.getSource() == selectMapFile)
    {
      DataEditOptionsPanel.this.selectMapFile(evt);
    }
  }// </editor-fold>//GEN-END:initComponents

  private void selectMapFile(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectMapFile
  {//GEN-HEADEREND:event_selectMapFile
    String fileName = FileDialogUtil.selectPkMapFile(this);
    if (fileName != null) pkMapFile.setText(fileName);
  }//GEN-LAST:event_selectMapFile


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JCheckBox checkEditable;
  private javax.swing.JTextField fieldLength;
  private javax.swing.JCheckBox highlightRequired;
  private javax.swing.JTextField inputLines;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JTextField pkMapFile;
  private javax.swing.JLabel pkMapFileLabel;
  private javax.swing.JCheckBox previewDml;
  private workbench.gui.components.WbColorPicker requiredFieldColor;
  private javax.swing.JButton selectMapFile;
  private javax.swing.JCheckBox warnDiscard;
  // End of variables declaration//GEN-END:variables

}
