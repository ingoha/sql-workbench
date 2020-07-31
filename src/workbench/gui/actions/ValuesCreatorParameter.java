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
package workbench.gui.actions;

import javax.swing.JPanel;

import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

/**
 *
 * @author Thomas Kellerer
 */
public class ValuesCreatorParameter
  extends JPanel
{
  private final String delimiterProp = "workbench.gui.values.creator.delimiter";
  private final String regexProp = "workbench.gui.values.creator.regex";

  public ValuesCreatorParameter()
  {
    initComponents();
    restoreSettings();
  }

  public String getDelimiter()
  {
    return delimiter.getText();
  }

  public boolean isRegex()
  {
    return isRegex.isSelected();
  }

  public void setFocusToInput()
  {
    this.delimiter.requestFocus();
    this.delimiter.selectAll();
  }

  public void restoreSettings()
  {
    String delim = Settings.getInstance().getProperty(delimiterProp, null);
    boolean regex = Settings.getInstance().getBoolProperty(regexProp, false);
    delimiter.setText(delim);
    isRegex.setSelected(regex);
  }

  public void saveSettings()
  {
    Settings.getInstance().setProperty(delimiterProp, delimiter.getText());
    Settings.getInstance().setProperty(regexProp, isRegex.isSelected());
  }


  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {
    java.awt.GridBagConstraints gridBagConstraints;

    jLabel1 = new javax.swing.JLabel();
    delimiter = new javax.swing.JTextField();
    isRegex = new javax.swing.JCheckBox();

    setLayout(new java.awt.GridBagLayout());

    jLabel1.setText(ResourceMgr.getString("LblFieldDelimiter")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    add(jLabel1, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 18);
    add(delimiter, gridBagConstraints);

    isRegex.setText(ResourceMgr.getString("LblDelimIsRegex")); // NOI18N
    isRegex.setBorder(null);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
    add(isRegex, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextField delimiter;
  private javax.swing.JCheckBox isRegex;
  private javax.swing.JLabel jLabel1;
  // End of variables declaration//GEN-END:variables
}
