package com.bounce.geflipping;

import com.bounce.geflipping.model.ItemSuggestion;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

class GeFlippingPanel extends PluginPanel
{
    private final JLabel coinsLabel = new JLabel();
    private final DefaultTableModel tableModel;

    GeFlippingPanel()
    {
        setLayout(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Coins:"));
        top.add(coinsLabel);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Item", "Qty", "Buy", "Sell", "Profit", "Total"}, 0)
        {
            @Override
            public Class<?> getColumnClass(int column)
            {
                return column == 0 ? Icon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    void setCoins(int coins)
    {
        coinsLabel.setText(String.valueOf(coins));
    }

    void setSuggestions(List<ItemSuggestion> suggestions)
    {
        tableModel.setRowCount(0);
        for (ItemSuggestion s : suggestions)
        {
            tableModel.addRow(new Object[]{
                s.getIcon(),
                s.getQuantity(),
                s.getBuyPrice(),
                s.getSellPrice(),
                s.getProfit(),
                s.getTotalProfit()
            });
        }
    }
}
