package com.amcio.mcsm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;

public class NIODownloader {
    private NIODownloader() { }

    public static void download(URL source, String dest) throws IOException {
        download(source, new File(dest));
    }

    public static void download(URL source, Path dest) throws IOException {
        download(source, dest.toFile());
    }
    public static void download(URL source, File dest) throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(source.openStream());
        try (FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        }
    }
}
