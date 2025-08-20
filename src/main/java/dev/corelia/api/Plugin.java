package dev.corelia.api;

import java.util.Objects;

public abstract class Plugin {
    private PluginInfo info;

    @SuppressWarnings("unused")
    protected Plugin() {
    }

    @SuppressWarnings("unused")
    protected Plugin(PluginInfo info) {
        this.info = Objects.requireNonNull(info, "info");
    }

    public final void init(PluginInfo info) {
        if (this.info != null) return;
        this.info = Objects.requireNonNull(info, "info");
    }

    @SuppressWarnings("unused")
    public PluginInfo getInfo() {
        return info;
    }

    public abstract void onLoad() throws Exception;
}