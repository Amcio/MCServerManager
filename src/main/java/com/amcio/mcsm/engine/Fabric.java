package com.amcio.mcsm.engine;
import com.amcio.mcsm.util.NIODownloader;
import com.amcio.mcsm.util.UnsafeURL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;

public class Fabric extends BaseMinecraftEngine {
    String API_ENDPOINT = "https://meta.fabricmc.net/v2/versions/loader/" + this.getVersion().toString();

    Fabric(String version, String rootDirectory) throws IllegalArgumentException {
        super(version, rootDirectory);
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.FABRIC;
    }
    private String getLatestLoaderVersion() throws IOException {
        URL baseURL = UnsafeURL.of(API_ENDPOINT);
        JsonNode latestBuild = new ObjectMapper().readTree(baseURL).get(0);
        if (latestBuild == null) {
            throw new UnsupportedOperationException("Version " + this.version.toString() + " is unsupported");
        }
        return latestBuild.get("loader").get("version").textValue();
    }
    @Override
    public void download() throws IOException {
        String loaderVersion = getLatestLoaderVersion();

        // I assume that the installer version will stay mostly the same
        // TODO: Figure out how to get this version with scraping as a last resort
        URL serverJarURL = UnsafeURL.of(String.join("/", API_ENDPOINT, loaderVersion, "0.11.2/server/jar"));

        HttpURLConnection httpConnection = (HttpURLConnection) serverJarURL.openConnection();
        httpConnection.setRequestMethod("HEAD");
        String jarName = httpConnection.getHeaderField("content-disposition")
                .split("=")[1]
                .trim()
                .replace("\"", "");
        httpConnection.disconnect();
        File finalPath = Path.of(rootDirectory, jarName).toFile();
        NIODownloader.download(serverJarURL, finalPath);
    }

    @Override
    public void install() {

    }
}
