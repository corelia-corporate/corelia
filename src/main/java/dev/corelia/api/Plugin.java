package dev.corelia.api;

import java.util.Objects;

public abstract class Plugin {
    private PluginInfo info;

    // Allow subclasses to omit constructors
    protected Plugin() {}

    // Backward compatibility for existing plugins
    protected Plugin(PluginInfo info) {
        this.info = Objects.requireNonNull(info, "info");
    }

    // Called by the plugin loader when using the no-arg constructor path
    public final void init(PluginInfo info) {
        if (this.info != null) return; // already initialized
        this.info = Objects.requireNonNull(info, "info");
    }

    public PluginInfo getInfo() {
        return info;
    }

    public abstract void onLoad() throws Exception;
}