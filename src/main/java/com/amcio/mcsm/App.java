package com.amcio.mcsm;
import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.Fabric;

import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException {
        ArgParser argParser = new ArgParser(args);
        String jarDestination = argParser.getPath(); // Either the path or "./", no need to worry about it later.

        Fabric fabricInstance = new Fabric("1.16.5");
        fabricInstance.download(jarDestination);
    }
}
