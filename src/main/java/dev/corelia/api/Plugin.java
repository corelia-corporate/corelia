package dev.corelia.api;

public abstract class Plugin {
    private final PluginInfo info;

    protected Plugin(PluginInfo info) {
        this.info = info;
    }

    public PluginInfo getInfo() {
        return info;
    }

    public abstract void onLoad() throws Exception;

    public abstract void onEnable() throws Exception;

    public abstract void onDisable() throws Exception;
}