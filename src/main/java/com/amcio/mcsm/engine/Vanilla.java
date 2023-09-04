package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.NIODownloader;
import com.amcio.mcsm.util.UnsafeURL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;


public class Vanilla extends BaseMinecraftEngine {
    String API_ENDPOINT = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public Vanilla(String version, String rootDirectory) throws IllegalArgumentException {
        super(version, rootDirectory);
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.VANILLA;
    }

    private String getVersionManifest() throws IOException {
        URL versionManifest = UnsafeURL.of(API_ENDPOINT);
        JsonNode manifestData = new ObjectMapper().readTree(versionManifest).get("versions");
        for (JsonNode version : manifestData) {
            if (version.get("id").textValue().equals(this.getVersion().toString())) {
                return version.get("url").textValue();
            }
        }
        throw new UnsupportedOperationException("Version " + this.version.toString() + " is unsupported");
    }

    private String getServerJarURL(String versionManifestURL) throws IOException {
        URL versionManifest = UnsafeURL.of(versionManifestURL);
        JsonNode manifestData = new ObjectMapper().readTree(versionManifest);
        System.out.println("[*] This version requires Java " + manifestData.at("/javaVersion/majorVersion").intValue());
        return manifestData.at("/downloads/server/url").textValue();
    }

    @Override
    public void download() throws IOException {
        String versionManifestURL = null;
        try {
            versionManifestURL = getVersionManifest();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        URL serverJarURL = UnsafeURL.of(getServerJarURL(versionManifestURL));
        String jarName = version.toString() + "-server.jar";
        File finalPath = Path.of(rootDirectory, jarName).toFile();
        NIODownloader.download(serverJarURL, finalPath);
    }

    @Override
    public void install() {

    }
}
