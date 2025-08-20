package dev.corelia.core;

import dev.corelia.api.Plugin;

import java.nio.file.Path;

public class App {
    public static void main(String[] args) throws Exception {
        Path pluginsDir = Path.of("plugins");
        PluginManager pm = new PluginManager(pluginsDir);

        pm.loadAll();
        pm.enableAll();

        System.out.println("Plugins loaded successfully :");
        for (Plugin p : pm.getPlugins()) {
            System.out.println(" - " + p.getInfo().name());
        }
    }
}