package com.bounce.geflipping;

import javax.swing.*;
import java.awt.*;

class GeFlippingPanel extends JPanel
{
    private final JLabel coinsLabel = new JLabel();
    private final JLabel suggestionLabel = new JLabel();

    GeFlippingPanel()
    {
        setLayout(new GridLayout(0, 1));
        add(new JLabel("Coins:"));
        add(coinsLabel);
        add(new JLabel("Suggested Item:"));
        add(suggestionLabel);
    }

    void setCoins(int coins)
    {
        coinsLabel.setText(String.valueOf(coins));
    }

    void setSuggestion(int itemId, Margin margin)
    {
        suggestionLabel.setText(itemId + " profit:" + margin.profit());
    }
}
