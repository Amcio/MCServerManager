package com.amcio.mcsm;
import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.*;

import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {
        ArgParser argParser = new ArgParser(args);
        String jarDestination = argParser.getPath(); // Either the path or "./", no need to worry about it later.

        MinecraftEngine Instance = new Vanilla("1.20.1");
        Instance.download(jarDestination);
    }
}
