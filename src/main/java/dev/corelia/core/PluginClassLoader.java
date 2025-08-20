package dev.corelia.core;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {
    public PluginClassLoader(URL jarUrl, ClassLoader parent) {
        super(new URL[]{jarUrl}, parent);
    }

    @Override
    public void close() {
        try {
            super.close();
        } catch (Exception ignored) {
        }
    }
}