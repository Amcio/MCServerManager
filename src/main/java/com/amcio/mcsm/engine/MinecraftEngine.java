package com.amcio.mcsm.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface MinecraftEngine {
    /**
     * @return One of the enum members
     * @see MinecraftEngineType
     */
    MinecraftEngineType getType();

    String getVersion();
    static boolean validateVersion(String version) {
        Matcher matcher = Pattern.compile("^\\d\\.\\d\\d?(\\.\\d\\d?)?$")
                .matcher(version);
        return matcher.matches();
    }
}
