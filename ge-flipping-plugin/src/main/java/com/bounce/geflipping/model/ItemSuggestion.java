package com.bounce.geflipping.model;

import javax.swing.ImageIcon;

public class ItemSuggestion
{
    private final int itemId;
    private final String name;
    private final ImageIcon icon;
    private final int quantity;
    private final int buyPrice;
    private final int sellPrice;
    private final int profit;
    private final int totalProfit;

    public ItemSuggestion(int itemId, String name, ImageIcon icon, int quantity,
                          int buyPrice, int sellPrice, int profit, int totalProfit)
    {
        this.itemId = itemId;
        this.name = name;
        this.icon = icon;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.profit = profit;
        this.totalProfit = totalProfit;
    }

    public int getItemId()
    {
        return itemId;
    }

    public String getName()
    {
        return name;
    }

    public ImageIcon getIcon()
    {
        return icon;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public int getBuyPrice()
    {
        return buyPrice;
    }

    public int getSellPrice()
    {
        return sellPrice;
    }

    public int getProfit()
    {
        return profit;
    }

    public int getTotalProfit()
    {
        return totalProfit;
    }
}
