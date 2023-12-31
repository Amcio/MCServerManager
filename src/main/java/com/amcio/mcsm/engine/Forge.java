package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.NIODownloader;
import com.amcio.mcsm.util.UnsafeURL;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Forge extends BaseMinecraftEngine {
    // In the future this class must hold information about the installer, and the installed server jar to run it later.
    String API_ENDPOINT = "https://maven.minecraftforge.net/net/minecraftforge/forge/";

    public Forge(String version, String rootDirectory) throws IllegalArgumentException {
        super(version, rootDirectory);
        if (this.version.isLessThan("1.6.4")) {
            throw new UnsupportedOperationException("Versions below 1.6.4 are not supported");
        }
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.FORGE;
    }

    private String getLatestVersion() throws IOException, XMLStreamException {
        URL mavenMetadataURL = UnsafeURL.of(API_ENDPOINT + "maven-metadata.xml");
        InputStream inputStream = mavenMetadataURL.openStream();

        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        // XXE attack?
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        XMLEventReader reader = xmlInputFactory.createXMLEventReader(inputStream);

        while(reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals("version")) {
                    event = reader.nextEvent(); // Move from <version> tag to the actual data
                    String forgeVersion = event.asCharacters().getData();
                    if (forgeVersion.split("-")[0].equals(this.getVersion().toString())) {
                        return forgeVersion; // The first version in the list is the latest
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void download() throws IOException {
        String forgeVersion = null;
        try {
            forgeVersion = getLatestVersion();
        } catch (XMLStreamException ignored) { }

        assert forgeVersion != null;

        jarName = String.format("forge-%s-installer.jar", forgeVersion);
        URL installerJarURL = UnsafeURL.of(String.format("%s%s/%s", API_ENDPOINT, forgeVersion, jarName));

        File finalPath = Path.of(rootDirectory, jarName).toFile();
        NIODownloader.download(installerJarURL, finalPath);
    }

    @Override
    public void install(String... args) throws IOException {
        List<String> arg = new ArrayList<>(List.of(args));  // ArrayList because List.of() is immutable.
        arg.add("--installServer");
        super.install(arg.toArray(new String[0]));
    }
}
