package com.bounce.geflipping;

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
import net.runelite.client.game.ItemManager;
import net.runelite.client.util.ImageUtil;
import javax.swing.ImageIcon;
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

    @Inject
    private ItemManager itemManager;

    private NavigationButton navButton;
    private GeFlippingPanel panel;

    static final String PRICE_URL = "https://prices.runescape.wiki/api/v1/osrs/latest";
    static final String VOLUME_URL = "https://prices.runescape.wiki/api/v1/osrs/5m";

    @Override
    protected void startUp() throws Exception
    {
        panel = new GeFlippingPanel();
        navButton = NavigationButton.builder()
                .tooltip("GE Flipping Helper")
                .icon(ImageUtil.loadImageResource(getClass(), "/geflipping_icon.png"))
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
            final int availableCoins = coins;
            Map<Integer, Margin> margins = GePriceFetcher.fetchMargins();
            Map<Integer, VolumeData> volumes = GePriceFetcher.fetchVolumes();

            List<Map.Entry<Integer, Margin>> sorted = margins.entrySet().stream()
                    .filter(e -> e.getValue().low > 0 && e.getValue().low <= availableCoins)
                    .sorted(Comparator.comparingInt(e -> {
                        int id = e.getKey();
                        Margin m = e.getValue();
                        VolumeData v = volumes.get(id);
                        int volLimit = v != null ? v.lowPriceVolume : Integer.MAX_VALUE;
                        int qty = Math.min(availableCoins / m.low, volLimit);
                        return -(qty * m.profit());
                    }))
                    .collect(Collectors.toList());

            for (Map.Entry<Integer, Margin> entry : sorted)
            {
                int id = entry.getKey();
                Margin margin = entry.getValue();
                VolumeData volume = volumes.get(id);
                int volLimit = volume != null ? volume.lowPriceVolume : Integer.MAX_VALUE;
                int quantity = Math.min(availableCoins / margin.low, volLimit);
                int totalProfit = quantity * margin.profit();
                panel.setSuggestion(
                    new ImageIcon(itemManager.getImage(id)),
                    itemManager.getItemComposition(id).getName(),
                    quantity,
                    margin,
                    totalProfit);
                break;
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
        return mapper.convertValue(data,
            new com.fasterxml.jackson.core.type.TypeReference<Map<Integer, Margin>>() {});
    }

    static Map<Integer, VolumeData> fetchVolumes() throws IOException
    {
        JsonNode node = mapper.readTree(new URL(GeFlippingPlugin.VOLUME_URL));
        JsonNode data = node.get("data");
        return mapper.convertValue(data,
            new com.fasterxml.jackson.core.type.TypeReference<Map<Integer, VolumeData>>() {});
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

class VolumeData
{
    public int avgHighPrice;
    public int highPriceVolume;
    public int avgLowPrice;
    public int lowPriceVolume;
}
