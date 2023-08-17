package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.UnsafeURL;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class Forge extends DummyMinecraftEngine implements MinecraftEngine {

    String API_ENDPOINT = "https://maven.minecraftforge.net/net/minecraftforge/forge/";

    public Forge(String version) throws IllegalArgumentException {
        super(version);
        if (this.version.isLessThan("1.6.4")) {
            throw new UnsupportedOperationException("Versions below 1.6.4 are not supported");
        }
    }

    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.FORGE;
    }

    private String getLatestVersion() throws IOException, XMLStreamException {
        URL mavenMetadataURL = UnsafeURL.create(API_ENDPOINT + "maven-metadata.xml");
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
    public void download(String dest) throws IOException {
        String forgeVersion = null;
        try {
            forgeVersion = getLatestVersion();
        } catch (XMLStreamException ignored) { }

        assert forgeVersion != null;

        String installerJarName = String.format("forge-%s-installer.jar", forgeVersion);
        URL installerJarURL = UnsafeURL.create(String.format("%s%s/%s", API_ENDPOINT, forgeVersion, installerJarName));

        String finalPath = String.join(System.getProperty("file.separator"), dest, installerJarName);
        ReadableByteChannel readableByteChannel = Channels.newChannel(installerJarURL.openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalPath)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }
}
