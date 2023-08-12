package com.amcio.mcsm.engine;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface MinecraftEngine {
    /**
     * @return One of the enum members
     * @see MinecraftEngineType
     */
    MinecraftEngineType getType();

    String getVersion();

    /**
     * Function that validates whether a string is a minecraft version number
     * @param version The version string to check
     * @return true if correct, false if invalid
     */
    static boolean validateVersion(String version) {
        Matcher matcher = Pattern.compile("^\\d\\.\\d\\d?(\\.\\d\\d?)?$")
                .matcher(version);
        return matcher.matches();
    }

    /**
     * Downloads the engine .jar file to the specified directory
     * @param dest The directory where the file is saved
     */
    void download(String dest) throws IOException;
}
