package com.bounce.geflipping;

import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

import javax.inject.Inject;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@PluginDescriptor(name = "GE Flipping Helper")
public class GeFlippingPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClientToolbar clientToolbar;

    private NavigationButton navButton;
    private GeFlippingPanel panel;

    private static final String PRICE_URL = "https://prices.runescape.wiki/api/v1/osrs/latest";

    @Override
    protected void startUp() throws Exception
    {
        panel = new GeFlippingPanel();
        navButton = NavigationButton.builder()
                .tooltip("GE Flipping Helper")
                .icon(new ImageIcon(getClass().getResource("/geflipping_icon.png")))
                .panel(panel)
                .build();
        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() throws Exception
    {
        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    private void onGameTick(GameTick tick)
    {
        ItemContainer inv = client.getItemContainer(net.runelite.api.InventoryID.INVENTORY);
        if (inv == null)
        {
            return;
        }

        int coins = 0;
        for (Item item : inv.getItems())
        {
            if (item.getId() == net.runelite.api.ItemID.COINS_995)
            {
                coins += item.getQuantity();
            }
        }
        panel.setCoins(coins);

        try
        {
            Map<Integer, Margin> margins = GePriceFetcher.fetchMargins();
            List<Map.Entry<Integer, Margin>> sorted = margins.entrySet().stream()
                    .sorted(Comparator.comparingInt(e -> -e.getValue().profit()))
                    .collect(Collectors.toList());
            for (Map.Entry<Integer, Margin> entry : sorted)
            {
                if (entry.getValue().high <= coins)
                {
                    panel.setSuggestion(entry.getKey(), entry.getValue());
                    break;
                }
            }
        }
        catch (IOException ex)
        {
            log.debug("Failed to fetch prices", ex);
        }
    }
}

class GePriceFetcher
{
    private static final ObjectMapper mapper = new ObjectMapper();
    static Map<Integer, Margin> fetchMargins() throws IOException
    {
        JsonNode node = mapper.readTree(new URL(GeFlippingPlugin.PRICE_URL));
        JsonNode data = node.get("data");
        return mapper.convertValue(data, Map.class);
    }
}

class Margin
{
    public int high;
    public int low;

    public int profit()
    {
        return high - low;
    }
}
