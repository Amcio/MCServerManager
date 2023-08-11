package com.amcio.mcsm.engine;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Fabric extends DummyMinecraftEngine implements MinecraftEngine {
    String API_ENDPOINT = "https://meta.fabricmc.net/v2/versions/loader/" + this.getVersion();
    public Fabric(String version) throws IllegalArgumentException {
        super(version);

    }
    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.FABRIC;
    }
    private String getLatestLoaderVersion() throws IOException {
        URL baseURL = null;
        try {
            baseURL = new URI(API_ENDPOINT).toURL();
        } catch (URISyntaxException ignored) { }

        return new ObjectMapper().readTree(baseURL)
                .get(0).get("loader").get("version")
                .textValue();
    }
    @Override
    public void download(String dest) throws IOException {
        String loaderVersion = getLatestLoaderVersion();
        URL serverJarURL = null;
        try {
            // I assume that the installer version will stay mostly the same
            // TODO: Figure out how to get this version with scraping as a last resort
            serverJarURL = new URI(String.join("/", API_ENDPOINT, loaderVersion, "0.11.2/server/jar")).toURL();
        } catch (URISyntaxException ignored) { }

        assert serverJarURL != null;
        HttpURLConnection httpConnection = (HttpURLConnection) serverJarURL.openConnection();
        httpConnection.setRequestMethod("HEAD");
        String filename = httpConnection.getHeaderField("content-disposition")
                .split("=")[1]
                .trim()
                .replace("\"", "");
        httpConnection.disconnect();
        String finalPath = String.join(System.getProperty("file.separator"), dest, filename);
        ReadableByteChannel readableByteChannel = Channels.newChannel(serverJarURL.openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalPath)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }

    }
}
