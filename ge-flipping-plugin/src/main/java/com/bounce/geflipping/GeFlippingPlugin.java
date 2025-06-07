package com.bounce.geflipping;

import com.bounce.geflipping.fetch.GePriceFetcher;
import com.bounce.geflipping.model.ItemSuggestion;
import com.bounce.geflipping.model.Margin;
import com.bounce.geflipping.model.VolumeData;
import com.bounce.geflipping.service.FlipCalculator;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private FlipCalculator calculator;
    private GePriceFetcher fetcher;

    static final String PRICE_URL = "https://prices.runescape.wiki/api/v1/osrs/latest";
    static final String VOLUME_URL = "https://prices.runescape.wiki/api/v1/osrs/5m";

    @Override
    protected void startUp()
    {
        panel = new GeFlippingPanel();
        calculator = new FlipCalculator(itemManager);
        fetcher = new GePriceFetcher(PRICE_URL, VOLUME_URL);
        navButton = NavigationButton.builder()
                .tooltip("GE Flipping Helper")
                .icon(ImageUtil.loadImageResource(getClass(), "/geflipping_icon.png"))
                .panel(panel)
                .build();
        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown()
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
            Map<Integer, Margin> margins = fetcher.fetchMargins();
            Map<Integer, VolumeData> volumes = fetcher.fetchVolumes();
            List<ItemSuggestion> suggestions = calculator.calculate(coins, margins, volumes);
            panel.setSuggestions(suggestions);
        }
        catch (IOException ex)
        {
            log.debug("Failed to fetch prices", ex);
        }
    }
}
