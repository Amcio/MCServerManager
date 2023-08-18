package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.UnsafeURL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Vanilla extends DummyMinecraftEngine implements MinecraftEngine {
    String API_ENDPOINT = "https://launchermeta.mojang.com/mc/game/version_manifest.json";

    public Vanilla(String version) throws IllegalArgumentException {
        super(version);
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.VANILLA;
    }

    private String getVersionManifest() throws IOException {
        URL versionManifest = UnsafeURL.create(API_ENDPOINT);
        JsonNode manifestData = new ObjectMapper().readTree(versionManifest).get("versions");
        for (JsonNode version : manifestData) {
            if (version.get("id").textValue().equals(this.getVersion().toString())) {
                return version.get("url").textValue();
            }
        }
        throw new UnsupportedOperationException("Version " + this.version.toString() + " is unsupported");
    }

    private String getServerJarURL(String versionManifestURL) throws IOException {
        URL versionManifest = UnsafeURL.create(versionManifestURL);
        JsonNode manifestData = new ObjectMapper().readTree(versionManifest);
        System.out.println("[*] This version requires Java " + manifestData.at("/javaVersion/majorVersion").intValue());
        return manifestData.at("/downloads/server/url").textValue();
    }

    @Override
    public void download(String dest) throws IOException {
        String versionManifestURL = null;
        try {
            versionManifestURL = getVersionManifest();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        URL serverJarURL = UnsafeURL.create(getServerJarURL(versionManifestURL));
        String jarName = version.toString() + "-server.jar";
        String finalPath = String.join(System.getProperty("file.separator"), dest, jarName);
        ReadableByteChannel readableByteChannel = Channels.newChannel(serverJarURL.openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalPath)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }
}
