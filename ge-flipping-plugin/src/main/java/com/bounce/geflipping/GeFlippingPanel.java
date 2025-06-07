package com.bounce.geflipping;

import net.runelite.client.ui.PluginPanel;
import javax.swing.*;
import java.awt.*;

class GeFlippingPanel extends PluginPanel
{
    private final JLabel coinsLabel = new JLabel();
    private final JLabel iconLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JLabel quantityLabel = new JLabel();
    private final JLabel buyLabel = new JLabel();
    private final JLabel sellLabel = new JLabel();
    private final JLabel profitLabel = new JLabel();
    private final JLabel totalProfitLabel = new JLabel();

    GeFlippingPanel()
    {
        setLayout(new GridLayout(0, 1));
        add(new JLabel("Coins:"));
        add(coinsLabel);
        add(new JLabel("Suggested Item:"));
        add(iconLabel);
        add(nameLabel);
        add(new JLabel("Quantity you can buy:"));
        add(quantityLabel);
        add(new JLabel("Buy price:"));
        add(buyLabel);
        add(new JLabel("Sell price:"));
        add(sellLabel);
        add(new JLabel("Profit per item:"));
        add(profitLabel);
        add(new JLabel("Total profit:"));
        add(totalProfitLabel);
    }

    void setCoins(int coins)
    {
        coinsLabel.setText(String.valueOf(coins));
    }

    void setSuggestion(ImageIcon icon, String name, int quantity, Margin margin, int totalProfit)
    {
        iconLabel.setIcon(icon);
        nameLabel.setText(name);
        quantityLabel.setText(String.valueOf(quantity));
        buyLabel.setText(String.valueOf(margin.low));
        sellLabel.setText(String.valueOf(margin.high));
        profitLabel.setText(String.valueOf(margin.profit()));
        totalProfitLabel.setText(String.valueOf(totalProfit));
    }
}
