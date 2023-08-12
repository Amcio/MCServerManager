package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.URLFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Paper extends DummyMinecraftEngine implements MinecraftEngine {

    String API_ENDPOINT = String.format("https://api.papermc.io/v2/projects/paper/versions/%s/builds/", this.getVersion());

    public Paper(String version) throws IllegalArgumentException {
        super(version);
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.PAPER;
    }

    @Override
    public void download(String dest) throws IOException {
        URL buildsEndpoint = URLFactory.create(API_ENDPOINT);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode builds = mapper.readTree(buildsEndpoint).get("builds");
        int latestBuild = builds.get(builds.size() - 1).get("build").intValue();
        System.out.println("Latest build: " + latestBuild);
        URL buildInfoURL = URLFactory.create(API_ENDPOINT + latestBuild);
        String buildProp = mapper.readTree(buildInfoURL).at("/downloads/application/name").textValue();

        URL serverJarURL = URLFactory.create(String.format("%s%s/downloads/%s",
                API_ENDPOINT,
                latestBuild,
                buildProp));
        String finalPath = String.join(System.getProperty("file.separator"), dest, buildProp);
        ReadableByteChannel readableByteChannel = Channels.newChannel(serverJarURL.openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalPath)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }
}
