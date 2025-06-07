package com.bounce.geflipping.fetch;

import com.bounce.geflipping.model.Margin;
import com.bounce.geflipping.model.VolumeData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class GePriceFetcher
{
    private static final ObjectMapper mapper = new ObjectMapper();

    private final String priceUrl;
    private final String volumeUrl;

    public GePriceFetcher(String priceUrl, String volumeUrl)
    {
        this.priceUrl = priceUrl;
        this.volumeUrl = volumeUrl;
    }

    public Map<Integer, Margin> fetchMargins() throws IOException
    {
        JsonNode node = mapper.readTree(new URL(priceUrl));
        JsonNode data = node.get("data");
        return mapper.convertValue(data,
            new com.fasterxml.jackson.core.type.TypeReference<Map<Integer, Margin>>() {});
    }

    public Map<Integer, VolumeData> fetchVolumes() throws IOException
    {
        JsonNode node = mapper.readTree(new URL(volumeUrl));
        JsonNode data = node.get("data");
        return mapper.convertValue(data,
            new com.fasterxml.jackson.core.type.TypeReference<Map<Integer, VolumeData>>() {});
    }
}
