package dev.corelia.api;

public interface Plugin {
    void onLoad() throws Exception;
    void onEnable() throws Exception;
    void onDisable() throws Exception;

    PluginDescription getDescription();
}
