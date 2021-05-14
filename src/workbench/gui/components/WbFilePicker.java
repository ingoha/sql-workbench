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
package workbench.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import workbench.log.CallerInfo;
import workbench.log.LogMgr;
import workbench.resource.Settings;

import workbench.gui.WbSwingUtilities;

import workbench.util.ExceptionUtil;
import workbench.util.StringUtil;

/**
 *
 * @author  Thomas Kellerer
 */
public class WbFilePicker
  extends JPanel
{
  public static final String PROP_FILENAME = "filename";
  private String lastDir;
  private FileFilter fileFilter;
  private boolean allowMultiple;
  private File[] selectedFiles;
  private String lastDirProperty;
  private boolean selectDirectory;
  private boolean showSaveAsDialog;
  private boolean showHiddenFiles;

  public WbFilePicker()
  {
    this(false);
  }
  public WbFilePicker(boolean saveAsDialog)
  {
    super();
    this.showSaveAsDialog = saveAsDialog;
    initComponents();
    WbSwingUtilities.adjustButtonWidth(selectFileButton,22,22);
  }

  public void setShowHiddenFiles(boolean flag)
  {
    this.showHiddenFiles = flag;
  }

  public void setSelectDirectoryOnly(boolean flag)
  {
    selectDirectory = flag;
  }

  public void setSelectedFile(File f)
  {
    if (f != null)
    {
      this.selectedFiles = new File[] { f };
      this.tfFilename.setText(f.toString());
    }
  }

  public boolean getSelectDirectoryOnly()
  {
    return selectDirectory;
  }

  public void setTextFieldPropertyName(String name)
  {
    this.tfFilename.setName(name);
  }

  public void setTextfieldTooltip(String text)
  {
    tfFilename.setToolTipText(text);
  }

  public void setButtonTooltip(String text)
  {
    selectFileButton.setToolTipText(text);
  }

  public void setLastDirProperty(String prop)
  {
    this.lastDirProperty = prop;
    this.lastDir = Settings.getInstance().getProperty(prop, null);
  }

  @Override
  public void setEnabled(boolean flag)
  {
    super.setEnabled(flag);
    this.tfFilename.setEnabled(flag);
    this.selectFileButton.setEnabled(flag);
  }

  @Override
  public void setToolTipText(String text)
  {
    super.setToolTipText(text);
    tfFilename.setToolTipText(text);
    selectFileButton.setToolTipText(text);
  }

  /**
   * Adds an ActionListener for the text field.
   * @param l
   */
  public void addActionListener(ActionListener l)
  {
    this.tfFilename.addActionListener(l);
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

    tfFilename = new StringPropertyEditor();
    selectFileButton = new FlatButton();

    setLayout(new GridBagLayout());

    tfFilename.setColumns(10);
    tfFilename.setHorizontalAlignment(JTextField.LEFT);
    tfFilename.setName("library"); // NOI18N
    tfFilename.addMouseListener(new TextComponentMouseListener());
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(tfFilename, gridBagConstraints);

    selectFileButton.setText("...");
    selectFileButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent evt)
      {
        selectFileButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.anchor = GridBagConstraints.WEST;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new Insets(0, 2, 0, 0);
    add(selectFileButton, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents
  private void selectFileButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_selectFileButtonActionPerformed
  {//GEN-HEADEREND:event_selectFileButtonActionPerformed
    try
    {
      JFileChooser jf = new WbFileChooser();
      if (selectDirectory)
      {
        jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      }
      else
      {
        jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jf.setMultiSelectionEnabled(allowMultiple);
        jf.setFileHidingEnabled(!this.showHiddenFiles);
        if (this.lastDir != null)
        {
          jf.setCurrentDirectory(new File(this.lastDir));
        }
        if (this.fileFilter != null)
        {
          jf.setFileFilter(this.fileFilter);
        }
      }

      int answer = JFileChooser.CANCEL_OPTION;

      if (showSaveAsDialog)
      {
        answer = jf.showSaveDialog(SwingUtilities.getWindowAncestor(this));
      }
      else
      {
        answer = jf.showOpenDialog(SwingUtilities.getWindowAncestor(this));
      }

      if (answer == JFileChooser.APPROVE_OPTION)
      {
        if (this.allowMultiple)
        {
          this.selectedFiles = jf.getSelectedFiles();
        }
        else
        {
          this.selectedFiles = new File[1];
          this.selectedFiles[0] = jf.getSelectedFile();
        }

        StringBuilder path = new StringBuilder(this.selectedFiles.length * 100);
        for (int i = 0; i < this.selectedFiles.length; i++)
        {
          if (this.selectedFiles.length > 1 && i > 0)
          {
            path.append(StringUtil.getPathSeparator());
          }
          path.append(this.selectedFiles[i].getAbsolutePath().trim());
        }
        String newValue = path.toString();
        String oldValue = tfFilename.getText();
        this.tfFilename.setText(newValue);
        if (this.lastDirProperty != null)
        {
          Settings.getInstance().setProperty(lastDirProperty, selectedFiles[0].getParent());
        }
        this.firePropertyChange(PROP_FILENAME, oldValue, newValue);
      }
    }
    catch (Throwable e)
    {
      LogMgr.logError(new CallerInfo(){}, "Error selecting file", e);
      WbSwingUtilities.showErrorMessage(ExceptionUtil.getDisplay(e));
    }
  }//GEN-LAST:event_selectFileButtonActionPerformed

  public String getFilename()
  {
    return tfFilename.getText();
  }

  public void setFilename(String name)
  {
    this.tfFilename.setText(name != null ? name : "");
    this.tfFilename.setCaretPosition(0);
  }

  public File getSelectedFile()
  {
    if (this.selectedFiles == null)
    {
      if (StringUtil.isNonBlank(getFilename()))
      {
        return new File(getFilename());
      }
      return null;
    }
    return this.selectedFiles[0];
  }

  public File[] getSelectedFiles()
  {
    if (!this.allowMultiple)
    {
      return null;
    }
    return this.selectedFiles;
  }

  public void setAllowMultiple(boolean flag)
  {
    this.allowMultiple = flag;
  }

  public void setFileFilter(FileFilter f)
  {
    this.fileFilter = f;
  }

  public FileFilter getFileFilter()
  {
    return fileFilter;
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private JButton selectFileButton;
  private JTextField tfFilename;
  // End of variables declaration//GEN-END:variables

}
