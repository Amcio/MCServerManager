package com.amcio.mcsm.engine;

public abstract class DummyMinecraftEngine implements MinecraftEngine {
    String version;
    protected DummyMinecraftEngine(String version) throws IllegalArgumentException {
        if (MinecraftEngine.validateVersion(version)) {
            this.version = version;
        } else {
            throw new IllegalArgumentException("Supplied version string is invalid!");
        }
    }

    @Override
    public String getVersion() {
        return version;
    }
}
