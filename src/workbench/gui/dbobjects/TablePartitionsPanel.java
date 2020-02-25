/*
 * This file is part of SQL Workbench/J, https://www.sql-workbench.eu
 *
 * Copyright 2002-2013, Thomas Kellerer
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
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.sql.Types;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import workbench.interfaces.Reloadable;
import workbench.interfaces.Resettable;
import workbench.resource.IconMgr;
import workbench.resource.ResourceMgr;

import workbench.db.PartitionLister;
import workbench.db.TableIdentifier;
import workbench.db.TablePartition;
import workbench.db.WbConnection;

import workbench.gui.WbSwingUtilities;
import workbench.gui.actions.ReloadAction;
import workbench.gui.components.DataStoreTableModel;
import workbench.gui.components.WbSplitPane;
import workbench.gui.components.WbTable;
import workbench.gui.components.WbToolbar;

import workbench.storage.DataStore;

import workbench.util.WbThread;

/**
 *
 * @author Thomas Kellerer
 */
public class TablePartitionsPanel
  extends JPanel
  implements Reloadable, Resettable, ListSelectionListener
{
  private TableIdentifier currentTable;
  private ReloadAction reload;

  private WbConnection dbConnection;
  private PartitionLister reader;

  private JScrollPane usedScroll;
  private WbTable mainPartitions;
  private WbTable subPartitions;

  private boolean isRetrieving;
  private WbSplitPane split;
  private JLabel partitionLabel;
  private JLabel subPartitionLabel;

  public TablePartitionsPanel()
  {
    super(new BorderLayout());
    mainPartitions = new WbTable(false, false, false);
    subPartitions = new WbTable(false, false, false);

    split = new WbSplitPane(JSplitPane.VERTICAL_SPLIT);

    JPanel usesPanel = new JPanel(new BorderLayout());
    partitionLabel = createTitleLabel("TxtPartitions");
    usesPanel.add(partitionLabel, BorderLayout.PAGE_START);
    usedScroll = new JScrollPane(mainPartitions);
    usesPanel.add(usedScroll, BorderLayout.CENTER);
    split.setTopComponent(usesPanel);

    JPanel usingPanel = new JPanel(new BorderLayout());
    subPartitionLabel = createTitleLabel("TxtSubPartitions");

    usingPanel.add(subPartitionLabel, BorderLayout.PAGE_START);
    JScrollPane scroll2 = new JScrollPane(subPartitions);
    usingPanel.add(scroll2, BorderLayout.CENTER);
    split.setBottomComponent(usingPanel);
    split.setDividerLocation(0.5);
    split.setDividerSize((int)(IconMgr.getInstance().getSizeForLabel() / 2));
    split.setDividerBorder(new EmptyBorder(0, 0, 0, 0));

    add(split, BorderLayout.CENTER);

    reload = new ReloadAction(this);
    reload.setEnabled(false);

    WbToolbar toolbar = new WbToolbar();
    toolbar.add(reload);
    add(toolbar, BorderLayout.PAGE_START);
    mainPartitions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    mainPartitions.getSelectionModel().addListSelectionListener(this);
  }

  @Override
  public void valueChanged(ListSelectionEvent e)
  {
    loadSubPartitions();
  }

  private JLabel createTitleLabel(String key)
  {
    JLabel title = new JLabel(ResourceMgr.getString(key));
    title.setOpaque(true);
    title.setBackground(Color.WHITE);
    title.setIconTextGap((int)(IconMgr.getInstance().getSizeForLabel() / 2));
    title.setHorizontalTextPosition(SwingConstants.LEADING);
    Font f = title.getFont();
    Font f2 = f.deriveFont(Font.BOLD);
    //title.setBorder();
    FontMetrics fm = title.getFontMetrics(f2);
    int fontHeight = fm.getHeight();

    int top = (int)(fontHeight / 3);
    int left = (int)(fontHeight / 5);
    title.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED), new EmptyBorder(top, left, top, left)));
    title.setFont(f2);
    return title;
  }

  public void dispose()
  {
    reset();
    if (mainPartitions != null) mainPartitions.dispose();
    if (subPartitions != null) subPartitions.dispose();
  }

  public void setCurrentTable(TableIdentifier tbl)
  {
    currentTable = tbl;
    reset();
  }

  public void setConnection(WbConnection conn)
  {
    reset();
    dbConnection = conn;
    reader = PartitionLister.Factory.createReader(conn);
    reload.setEnabled(reader != null);
  }

  @Override
  public void reset()
  {
    WbSwingUtilities.invoke(() ->
    {
      mainPartitions.reset();
      subPartitions.reset();
    });
  }

  public void cancel()
  {
  }

  @Override
  public void reload()
  {
    reset();

    if (!WbSwingUtilities.isConnectionIdle(this, dbConnection)) return;

    WbThread loader = new WbThread(this::doLoad, "Partition Retrieval Thread");
    loader.start();
  }

  public void doLoad()
  {
    if (reader == null) return;
    if (isRetrieving) return;

    try
    {
      isRetrieving = true;
      reload.setEnabled(false);
      WbSwingUtilities.showWaitCursor(this);

      List<? extends TablePartition> partitions = reader.getPartitions(currentTable);

      EventQueue.invokeLater(() ->
      {
        showPartitions(partitions, mainPartitions);
      });

      EventQueue.invokeLater(this::calculateSplit);
    }
    finally
    {
      WbSwingUtilities.showDefaultCursor(this);
      reload.setEnabled(true);
      isRetrieving = false;
    }
  }

  private void loadSubPartitions()
  {
    this.subPartitions.reset();
    if (reader == null) return;

    if (!WbSwingUtilities.isConnectionIdle(this, dbConnection)) return;

    WbThread loader = new WbThread(this::doRetrieveSubPartitions, "SubPartition Retrieval Thread");
    loader.start();
  }

  private void doRetrieveSubPartitions()
  {
    if (isRetrieving) return;
    int selectedRow = mainPartitions.getSelectedRow();
    if (selectedRow < 0) return;

    TablePartition mainPart = (TablePartition)mainPartitions.getDataStore().getRow(selectedRow).getUserObject();
    if (mainPart == null) return;

    try
    {
      isRetrieving = true;
      reload.setEnabled(false);
      WbSwingUtilities.showWaitCursor(this);

      List<? extends TablePartition> partitions = reader.getSubPartitions(currentTable, mainPart);

      EventQueue.invokeLater(() ->
      {
        showPartitions(partitions, subPartitions);
      });
    }
    finally
    {
      WbSwingUtilities.showDefaultCursor(this);
      reload.setEnabled(true);
      isRetrieving = false;
    }
  }

  private void calculateSplit()
  {
    invalidate();
    int rows = Math.max(mainPartitions.getRowCount() + 2, 5);
    int height = (int)((mainPartitions.getRowHeight() * rows) * 1.10);
    int minHeight = (int)getHeight() / 5;
    split.setDividerLocation(Math.max(minHeight, height));
    doLayout();
  }

  private void showPartitions(List<? extends TablePartition> partitions, WbTable display)
  {
    String[] columns = new String[] {"PARTITION", "DEFINITION"};
    int[] sizes = new int[] {30, 60 };
    int[] types = new int[] {Types.VARCHAR, Types.VARCHAR };

    DataStore ds = new DataStore(columns, types, sizes);
    for (TablePartition part : partitions)
    {
      int row = ds.addRow();
      ds.setValue(row, 0, part.getObjectName());
      ds.setValue(row, 1, part.getDefinition());
      ds.getRow(row).setUserObject(part);
    }
    ds.resetStatus();
    DataStoreTableModel model = new DataStoreTableModel(ds);
    display.setModel(model, true);
  }

}
