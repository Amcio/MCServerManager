package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.Version;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BaseMinecraftEngine {
    Version version;
    String rootDirectory;
    /**
     * Runnable jar that we use to install the server, in case of Forge this will be the installer.
     */
    String jarName = null;

    BaseMinecraftEngine(String version, String rootDirectory) throws IllegalArgumentException {
        this.version = new Version(version);
        this.rootDirectory = rootDirectory;
    }

    protected void acceptEULA() throws IOException {
        System.out.println("[*] Auto accepting the EULA");
        Path eulaFile = Path.of(rootDirectory, "eula.txt");
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
    public void install(String... args) throws IOException {
        int status;
        List<String> command = Stream.of("java", "-jar", jarName).collect(Collectors.toList());
        if (!(args == null || args.length == 0)) {
            command.addAll(List.of(args));
        }
        ProcessBuilder pb = new ProcessBuilder(command)
                .inheritIO()
                .directory(new File(rootDirectory));
        System.out.println("[*] Running the server jar");
        Process p = pb.start();
        try {
            status = p.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[*] Server process exited with code " + status);
        try {
            acceptEULA();
        } catch (NoSuchFileException e) {
            System.out.println("[!!!] Cannot find eula.txt. Please accept it manually after first server start.");
        }
    }

    public abstract MinecraftEngineType getType();

    public Version getVersion() {
        return version;
    }
}
