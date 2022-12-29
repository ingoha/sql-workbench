/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2023 Thomas Kellerer
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
package workbench.gui.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.StringWriter;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;

import workbench.WbManager;
import workbench.console.DataStorePrinter;
import workbench.console.TextPrinter;
import workbench.log.CallerInfo;
import workbench.log.LogMgr;
import workbench.resource.ResourceMgr;
import workbench.resource.Settings;

import workbench.db.ConnectionInfoBuilder;
import workbench.db.DriverInfo;
import workbench.db.WbConnection;

import workbench.gui.WbSwingUtilities;
import workbench.gui.components.*;

import workbench.storage.DataStore;

import workbench.util.StringUtil;

/**
 *
 * @author  Thomas Kellerer
 */
public class ConnectionInfoPanel
  extends JPanel
{
  private WbTable extendedInfoData;

  public ConnectionInfoPanel(WbConnection conn)
  {
    super();
    initComponents();

    try
    {
      ConnectionInfoBuilder info = new ConnectionInfoBuilder();

      infotext.setContentType("text/html");
      infotext.setFont(Settings.getInstance().getEditorFont());
      infotext.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
      infotext.setText(info.getHtmlDisplay(conn));
      infotext.setCaretPosition(0);
      infotext.setBorder(WbSwingUtilities.EMPTY_BORDER);
      TextComponentMouseListener.addListener(infotext);
      FontMetrics fm = infotext.getFontMetrics(infotext.getFont());
      int height = fm.getHeight() * 16;
      int width = fm.getMaxAdvance() * 75;
      Dimension d = new Dimension(width, height);
      jScrollPane1.setSize(d);
      jScrollPane1.setPreferredSize(d);
      infoTabs.setTitleAt(0, ResourceMgr.getString("TxtInfoBasic"));

      if (conn.isBusy())
      {
        infoTabs.remove(1);
      }
      else
      {
        DriverInfo drvInfo = new DriverInfo(conn.getSqlConnection());
        extendedInfoData = new WbTable(true, false, false);
        WbScrollPane scroll = new WbScrollPane(extendedInfoData);
        scroll.setPreferredSize(d);
        DataStoreTableModel ds = new DataStoreTableModel(drvInfo.getInfo());
        extendedInfoData.setModel(ds, true);
        extendedPanel.add(scroll, BorderLayout.CENTER);
        infoTabs.setTitleAt(1, ResourceMgr.getString("TxtInfoExt"));
      }
    }
    catch (Exception e)
    {
      LogMgr.logError(new CallerInfo(){}, "Could not read connection properties", e);
    }
  }

  public void dispose()
  {
    if (this.extendedInfoData != null)
    {
      this.extendedInfoData.dispose();
    }
  }

  public static void showConnectionInfo(WbConnection con)
  {
    ConnectionInfoPanel p = new ConnectionInfoPanel(con);
    JFrame f = WbManager.getInstance().getCurrentWindow();
    ValidatingDialog d = new ValidatingDialog(f, ResourceMgr.getString("LblConnInfo"), p, false);
    d.pack();
    WbSwingUtilities.center(d, f);
    d.setVisible(true);
    p.dispose();
    d.dispose();
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

    infoTabs = new WbTabbedPane();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane1 = new WbScrollPane();
    infotext = new InfoEditorPane();
    copyButton = new javax.swing.JButton();
    extendedPanel = new javax.swing.JPanel();

    setLayout(new java.awt.GridBagLayout());

    jPanel1.setLayout(new java.awt.GridBagLayout());

    infotext.setEditable(false);
    infotext.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
    infotext.setContentType("text/html"); // NOI18N
    infotext.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
    jScrollPane1.setViewportView(infotext);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    jPanel1.add(jScrollPane1, gridBagConstraints);

    copyButton.setText(ResourceMgr.getString("LblCopyToClp")); // NOI18N
    copyButton.setToolTipText(ResourceMgr.getString("d_LblCopyToClp")); // NOI18N
    copyButton.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        copyButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    jPanel1.add(copyButton, gridBagConstraints);

    infoTabs.addTab("Basic", jPanel1);

    extendedPanel.setLayout(new java.awt.BorderLayout());
    infoTabs.addTab("Extended", extendedPanel);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(infoTabs, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
    String content = infotext.getText();
    copyText(content, true);
  }//GEN-LAST:event_copyButtonActionPerformed

  public void copyText(String content, boolean copyExtended)
  {
    String clean = content;
    if (copyExtended)
    {
      clean = content.replaceAll(StringUtil.REGEX_CRLF, " ");
    }
    clean = clean.replaceAll(" {2,}", "");
    clean = clean.replaceAll("<br>", "\r\n");
    clean = clean.replaceAll("<div[0-9 a-zA-Z;=\\-\":]*>", "");
    clean = clean.replaceAll("</div>", "\r\n");
    clean = clean.replaceAll("<[/a-z]*>", "").trim();

    if (extendedInfoData != null && copyExtended)
    {
      DataStore ds = extendedInfoData.getDataStore();
      DataStorePrinter printer = new DataStorePrinter(ds);
      printer.setPrintRowCount(false);

      StringWriter sw = new StringWriter(100);
      TextPrinter pw = TextPrinter.createPrinter(sw);
      printer.printTo(pw);
      clean += "\n\n" + sw.toString();
    }

    Clipboard clp = Toolkit.getDefaultToolkit().getSystemClipboard();
    StringSelection sel = new StringSelection(clean);
    clp.setContents(sel, sel);
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton copyButton;
  private javax.swing.JPanel extendedPanel;
  private javax.swing.JTabbedPane infoTabs;
  private javax.swing.JEditorPane infotext;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables

  private class InfoEditorPane
    extends JEditorPane
  {
    InfoEditorPane()
    {
      super();
    }

    @Override
    public void copy()
    {
      String content = getSelectedText();
      if (content == null)
      {
        content = getText();
      }
      copyText(content, false);
    }
  }
}
