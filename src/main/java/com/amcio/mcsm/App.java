package com.amcio.mcsm;
import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.Fabric;
import com.amcio.mcsm.engine.Forge;
import com.amcio.mcsm.engine.MinecraftEngine;
import com.amcio.mcsm.engine.Paper;

import java.io.IOException;

public class App {

    private static void printUsage(String[] args) {
        System.out.println("Usage: " + args[0] + " -e <engine> -v <version> PATH");
    }
    public static void main( String[] args ) throws IOException {
        ArgParser argParser = new ArgParser(args);
        String jarDestination = argParser.getPath(); // Either the path or "./", no need to worry about it later.

        String selectedVersion = argParser.getValue("-v");
        String selectedEngine = argParser.getValue("-e");

        if (selectedVersion == null || selectedEngine == null) {
            printUsage(args);
            System.exit(1);
        }

        MinecraftEngine engine = switch (selectedEngine) {
            case "forge" -> new Forge(selectedVersion);
            case "fabric" -> new Fabric(selectedVersion);
            case "vanilla" -> throw new UnsupportedOperationException("Downloading vanilla jar is not yet supported");
            default -> new Paper(selectedVersion);
        };

        engine.download(jarDestination);
    }
}
