package com.amcio.mcsm;
import com.amcio.mcsm.argparser.ArgParser;
import com.amcio.mcsm.engine.*;
import com.amcio.mcsm.util.UnsafeURL;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;

public class App {
    public static void main(String[] args) throws IOException {
        ArgParser argParser = new ArgParser(new String[]{"test/"});
        String jarDestination = argParser.getPath(); // Either the path or "./", no need to worry about it later.
        Method mainMethod = null;
//        BaseMinecraftEngine Instance = new Vanilla("1.20.1", jarDestination);
//        Instance.download();
        //TODO: Load the whole jar into the classpath
        URL url = new File(jarDestination, "forge-1.20.1-47.1.44-installer.jar").toURI().toURL();
        String mainClass = null;
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url})) {
            URL manifestURL = urlClassLoader.findResource("META-INF/MANIFEST.MF");
            Manifest manifest = new Manifest(manifestURL.openStream());
            mainClass = manifest.getMainAttributes().getValue("Main-Class");
            mainMethod = urlClassLoader.loadClass(mainClass).getMethod("main", String[].class);
        } catch (ClassNotFoundException e) {
            System.err.println("[!!!] Class: " + mainClass + " not found in JAR file.");
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            mainMethod.invoke(null, (Object) args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}