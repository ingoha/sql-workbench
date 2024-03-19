/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2024 Thomas Kellerer
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import workbench.interfaces.Restoreable;
import workbench.resource.GuiSettings;
import workbench.resource.IconMgr;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

import workbench.gui.components.WbFontPicker;

/**
 *
 * @author Thomas Kellerer
 */
public class FontOptionsPanel
  extends JPanel
  implements Restoreable
{

  public FontOptionsPanel()
  {
    initComponents();
    standardFont.setAllowFontReset(true);
    editorFont.setAllowFontReset(true);
    editorFont.setListMonospacedOnly(true);
    dataFont.setAllowFontReset(true);
    msgLogFont.setAllowFontReset(true);
    String[] sizeValues = new String[]{ResourceMgr.getString("LblDefaultIndicator"), "16px", "24px", "32px"};
    DefaultComboBoxModel model = new DefaultComboBoxModel(sizeValues);
    iconSize.setModel(model);
  }

  @Override
  public void restoreSettings()
  {
    editorFont.setSelectedFont(Settings.getInstance().getEditorFont(false));
    dataFont.setSelectedFont(Settings.getInstance().getDataFont());
    msgLogFont.setSelectedFont(Settings.getInstance().getMsgLogFont());
    standardFont.setSelectedFont(Settings.getInstance().getStandardFont());
    wheelZoom.setSelected(GuiSettings.getZoomFontWithMouseWheel());

    scaleFonts.setSelected(Settings.getInstance().getScaleFonts());

    if (Settings.getInstance().getScaleMenuIcons())
    {
      iconSize.setSelectedIndex(0);
    }
    else
    {
      int size = Settings.getInstance().getToolbarIconSize();
      switch (size)
      {
        case IconMgr.SMALL_ICON:
          iconSize.setSelectedIndex(1);
          break;
        case IconMgr.MEDIUM_ICON:
          iconSize.setSelectedIndex(2);
          break;
        case IconMgr.LARGE_ICON:
          iconSize.setSelectedIndex(3);
          break;
      }
    }
  }

  @Override
  public void saveSettings()
  {
    Settings.getInstance().setEditorFont(editorFont.getSelectedFont());
    Settings.getInstance().setDataFont(dataFont.getSelectedFont());
    Settings.getInstance().setStandardFont(standardFont.getSelectedFont());
    Settings.getInstance().setMsgLogFont(msgLogFont.getSelectedFont());
    Settings.getInstance().setScaleFonts(scaleFonts.isSelected());
    GuiSettings.setZoomFontWithMouseWheel(wheelZoom.isSelected());
    int selected = iconSize.getSelectedIndex();
    switch (selected)
    {
      case 0:
        Settings.getInstance().setScaleMenuIcons(true);
        break;
      case 1:
        Settings.getInstance().setScaleMenuIcons(false);
        Settings.getInstance().setToolbarIconSize(IconMgr.SMALL_ICON);
        break;
      case 2:
        Settings.getInstance().setScaleMenuIcons(false);
        Settings.getInstance().setToolbarIconSize(IconMgr.MEDIUM_ICON);
        break;
      case 3:
        Settings.getInstance().setScaleMenuIcons(false);
        Settings.getInstance().setToolbarIconSize(IconMgr.LARGE_ICON);
        break;
    }
  }

  /** This method is called from within the constructor to  initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {
    GridBagConstraints gridBagConstraints;

    dataFontLabel = new JLabel();
    dataFont = new WbFontPicker();
    standardFont = new WbFontPicker();
    standardFontLabel = new JLabel();
    msgFontLabel = new JLabel();
    msgLogFont = new WbFontPicker();
    editorFont = new WbFontPicker();
    editorFontLabel = new JLabel();
    scaleFonts = new JCheckBox();
    wheelZoom = new JCheckBox();
    iconSizePanel = new JPanel();
    jLabel1 = new JLabel();
    iconSize = new JComboBox<>();

    setLayout(new GridBagLayout());

    dataFontLabel.setText(ResourceMgr.getString("LblDataFont")); // NOI18N
    dataFontLabel.setToolTipText(ResourceMgr.getString("d_LblDataFont")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(0, 1, 5, 0);
    add(dataFontLabel, gridBagConstraints);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 8, 5, 15);
    add(dataFont, gridBagConstraints);

    standardFont.setFont(standardFont.getFont());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 8, 5, 15);
    add(standardFont, gridBagConstraints);

    standardFontLabel.setText(ResourceMgr.getString("LblStandardFont")); // NOI18N
    standardFontLabel.setToolTipText(ResourceMgr.getString("d_LblStandardFont")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(0, 1, 5, 0);
    add(standardFontLabel, gridBagConstraints);

    msgFontLabel.setText(ResourceMgr.getString("LblMsgLogFont")); // NOI18N
    msgFontLabel.setToolTipText(ResourceMgr.getString("d_LblMsgLogFont")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(0, 1, 5, 0);
    add(msgFontLabel, gridBagConstraints);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(0, 8, 5, 15);
    add(msgLogFont, gridBagConstraints);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new Insets(3, 8, 5, 15);
    add(editorFont, gridBagConstraints);

    editorFontLabel.setText(ResourceMgr.getString("LblEditorFont")); // NOI18N
    editorFontLabel.setToolTipText(ResourceMgr.getString("d_LblEditorFont")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.insets = new Insets(3, 1, 5, 0);
    add(editorFontLabel, gridBagConstraints);

    scaleFonts.setText(ResourceMgr.getString("LblScaleFont")); // NOI18N
    scaleFonts.setToolTipText(ResourceMgr.getString("d_LblScaleFont")); // NOI18N
    scaleFonts.setBorder(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new Insets(7, 1, 5, 0);
    add(scaleFonts, gridBagConstraints);

    wheelZoom.setText(ResourceMgr.getString("LblEnableWheelZoom")); // NOI18N
    wheelZoom.setBorder(null);
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new Insets(7, 1, 5, 0);
    add(wheelZoom, gridBagConstraints);

    iconSizePanel.setLayout(new GridBagLayout());

    jLabel1.setText(ResourceMgr.getString("LblIconSize")); // NOI18N
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.anchor = GridBagConstraints.LINE_START;
    gridBagConstraints.insets = new Insets(0, 0, 0, 11);
    iconSizePanel.add(jLabel1, gridBagConstraints);

    iconSize.setModel(new DefaultComboBoxModel<>(new String[] { "16 px", "24 px", "32 px", " " }));
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.anchor = GridBagConstraints.LINE_START;
    gridBagConstraints.weightx = 1.0;
    iconSizePanel.add(iconSize, gridBagConstraints);

    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(4, 0, 0, 0);
    add(iconSizePanel, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private WbFontPicker dataFont;
  private JLabel dataFontLabel;
  private WbFontPicker editorFont;
  private JLabel editorFontLabel;
  private JComboBox<String> iconSize;
  private JPanel iconSizePanel;
  private JLabel jLabel1;
  private JLabel msgFontLabel;
  private WbFontPicker msgLogFont;
  private JCheckBox scaleFonts;
  private WbFontPicker standardFont;
  private JLabel standardFontLabel;
  private JCheckBox wheelZoom;
  // End of variables declaration//GEN-END:variables
}
