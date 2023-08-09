package com.amcio.mcsm.engine;

public class Fabric extends DummyMinecraftEngine implements MinecraftEngine {
    public Fabric(String version) throws IllegalArgumentException {
        super(version);

    }
    @Override
    public MinecraftEngineType getType() {
        return MinecraftEngineType.FABRIC;
    }
}
