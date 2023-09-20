package com.amcio.mcsm;
import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.*;

import java.io.File;
import java.io.IOException;

public class App {
    public static void main( String[] args ) throws IOException, InterruptedException {
        ArgParser argParser = new ArgParser(new String[] {"test"});
        String jarDestination = argParser.getPath(); // Either the path or "./", no need to worry about it later.

        BaseMinecraftEngine Instance = new Forge("1.20.1", jarDestination);
        System.out.println("[*] Will download server of type: " + Instance.getType().toString());
        Instance.download();
        Instance.install();
    }
}
