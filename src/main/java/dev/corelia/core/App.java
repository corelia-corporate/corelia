package dev.corelia.core;

import java.nio.file.Path;

public final class App {
    public static void main(String[] args) throws Exception {
        Path pluginsDir = Path.of("plugins");
        PluginManager pm = new PluginManager(pluginsDir);

        pm.loadAll();
    }
}