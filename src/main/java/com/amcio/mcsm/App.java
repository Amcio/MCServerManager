package com.amcio.mcsm;
import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.Fabric;
import com.amcio.mcsm.engine.Forge;
import com.amcio.mcsm.engine.Paper;
import com.amcio.mcsm.engine.*;

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

        BaseMinecraftEngine Instance = switch (selectedEngine) {
            case "forge" -> new Forge(selectedVersion, jarDestination);
            case "fabric" -> new Fabric(selectedVersion, jarDestination);
            case "vanilla" -> throw new UnsupportedOperationException("Downloading vanilla jar is not yet supported");
            default -> new Paper(selectedVersion, jarDestination);
        };
        System.out.println("[*] Will download server of type: " + Instance.getType().toString());
        Instance.download();
        Instance.install();
    }
}
