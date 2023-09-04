package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.Version;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseMinecraftEngine {
    Version version;
    String rootDirectory;

    BaseMinecraftEngine(String version, String rootDirectory) throws IllegalArgumentException {
        this.version = new Version(version);
        this.rootDirectory = rootDirectory;
    }

    protected void acceptEULA() throws IOException {
        Path eulaFile = FileSystems.getDefault().getPath(rootDirectory, "eula.txt");
        List<String> newLines = new ArrayList<>();
        for (String line : Files.readAllLines(eulaFile, StandardCharsets.US_ASCII)) {
            if (!line.equals("eula=false")) {
                newLines.add(line);
            } else {
                newLines.add("eula=true");
            }
        }
        Files.write(eulaFile, newLines, StandardCharsets.US_ASCII);
    }

    /**
     * Downloads the engine .jar file to the root directory associated with the object
     */
    public abstract void download() throws IOException;

    /**
     * Will run the main class for the first time, so all basic files are created. acceptEULA() should be called from here.
     */
    public abstract void install();

    public abstract MinecraftEngineType getType();

    public Version getVersion() {
        return version;
    }
}
