package com.bounce.geflipping.service;

import com.bounce.geflipping.model.ItemSuggestion;
import com.bounce.geflipping.model.Margin;
import com.bounce.geflipping.model.VolumeData;
import net.runelite.client.game.ItemManager;

import javax.swing.ImageIcon;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlipCalculator
{
    private final ItemManager itemManager;

    public FlipCalculator(ItemManager itemManager)
    {
        this.itemManager = itemManager;
    }

    public List<ItemSuggestion> calculate(int coins, Map<Integer, Margin> margins,
                                          Map<Integer, VolumeData> volumes)
    {
        return margins.entrySet().stream()
                .filter(e -> e.getValue().low > 0 && e.getValue().low <= coins)
                .map(e -> toSuggestion(e.getKey(), e.getValue(), volumes.get(e.getKey()), coins))
                .sorted(Comparator.comparingInt(ItemSuggestion::getTotalProfit).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private ItemSuggestion toSuggestion(int id, Margin m, VolumeData v, int coins)
    {
        int volLimit = v != null ? v.lowPriceVolume : Integer.MAX_VALUE;
        int quantity = Math.min(coins / m.low, volLimit);
        int profit = m.profit();
        int total = quantity * profit;
        return new ItemSuggestion(
                id,
                itemManager.getItemComposition(id).getName(),
                new ImageIcon(itemManager.getImage(id)),
                quantity,
                m.low,
                m.high,
                profit,
                total);
    }
}
