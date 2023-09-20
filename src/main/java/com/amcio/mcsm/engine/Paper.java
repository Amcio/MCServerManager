package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.NIODownloader;
import com.amcio.mcsm.util.UnsafeURL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public class Paper extends BaseMinecraftEngine {

    String API_ENDPOINT = String.format("https://api.papermc.io/v2/projects/paper/versions/%s/builds/", this.getVersion().toString());

    public Paper(String version, String rootDirectory) throws IllegalArgumentException {
        super(version, rootDirectory);
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.PAPER;
    }

    @Override
    public void download() throws IOException {
        URL buildsEndpoint = UnsafeURL.of(API_ENDPOINT);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode builds = mapper.readTree(buildsEndpoint).get("builds");
        if (builds == null) {
            throw new UnsupportedOperationException("Version " + this.version.toString() + " is unsupported");
        }
        int latestBuild = builds.get(builds.size() - 1).get("build").intValue();
        System.out.println("[PAPER] Latest build: " + latestBuild);
        URL buildInfoURL = UnsafeURL.of(API_ENDPOINT + latestBuild);
        String buildProp = mapper.readTree(buildInfoURL).at("/downloads/application/name").textValue();

        URL serverJarURL = UnsafeURL.of(String.format("%s%s/downloads/%s",
                API_ENDPOINT,
                latestBuild,
                buildProp));
        File finalPath = Path.of(rootDirectory, buildProp).toFile();
        NIODownloader.download(serverJarURL, finalPath);
    }
}
