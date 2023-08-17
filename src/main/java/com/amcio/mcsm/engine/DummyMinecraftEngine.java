package com.amcio.mcsm.engine;

import com.amcio.mcsm.util.Version;

public abstract class DummyMinecraftEngine implements MinecraftEngine {
    Version version;
    protected DummyMinecraftEngine(String version) throws IllegalArgumentException {
        this.version = new Version(version);
    }

    @Override
    public Version getVersion() {
        return version;
    }
}
