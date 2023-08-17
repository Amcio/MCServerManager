package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.Version;
import java.io.IOException;

public interface MinecraftEngine {
    /**
     * @return One of the enum members
     * @see MinecraftEngineType
     */
    MinecraftEngineType getType();

    Version getVersion();

    /**
     * Downloads the engine .jar file to the specified directory
     * @param dest The directory where the file is saved
     */
    void download(String dest) throws IOException;
}
