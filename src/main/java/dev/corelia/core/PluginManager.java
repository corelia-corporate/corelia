package dev.corelia.core;

import dev.corelia.api.Plugin;
import dev.corelia.api.PluginInfo;
import dev.corelia.api.PluginException;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {
    private final Path pluginsDir;
    private final Map<String, LoadedPlugin> plugins = new LinkedHashMap<>();

    public PluginManager(Path pluginsDir) {
        this.pluginsDir = Objects.requireNonNull(pluginsDir);
    }

    public void loadAll() throws Exception {
        if (!Files.exists(pluginsDir)) Files.createDirectories(pluginsDir);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(pluginsDir, "*.jar")) {
            for (Path jar : stream) {
                try {
                    loadPlugin(jar);
                } catch (Exception e) {
                    Logger.warn("Fail to load " + jar.getFileName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void enableAll() {
        for (LoadedPlugin lp : plugins.values()) {
            try {
                lp.instance.onEnable();
                Logger.info("[PluginManager] Enabled: " + lp.instance.getInfo().description());
            } catch (Exception e) {
                Logger.warn("[PluginManager] Fail to enabled " + lp.info.name() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void disableAll() {
        List<LoadedPlugin> reverse = new ArrayList<>(plugins.values());
        Collections.reverse(reverse);
        for (LoadedPlugin lp : reverse) {
            try {
                lp.instance.onDisable();
                Logger.info("[PluginManager] Disabled: " + lp.instance.getInfo().description());
            } catch (Exception e) {
                Logger.warn("[PluginManager] Fail to disabled " + lp.info.name() + ": " + e.getMessage());
            }
            try {
                lp.classLoader.close();
            } catch (Exception ignored) {
            }
        }
        plugins.clear();
    }

    private void loadPlugin(Path jarPath) throws Exception {
        if (!Files.isRegularFile(jarPath)) return;

        try (JarFile jar = new JarFile(jarPath.toFile())) {
            JarEntry descEntry = Optional.ofNullable(jar.getJarEntry("plugin.yml")).orElseThrow(() -> new PluginException("plugin.yml missing"));

            Map<String, Object> meta;
            try (InputStream in = jar.getInputStream(descEntry)) {
                meta = loadYamlMap(in);
            }

            String name = required(meta, "name");
            String version = required(meta, "version");
            String description = required(meta, "description");
            String mainClass = required(meta, "main");

            PluginInfo info = new PluginInfo(name, version, description, mainClass);

            URL jarUrl = jarPath.toUri().toURL();
            PluginClassLoader pcl = new PluginClassLoader(jarUrl, getClass().getClassLoader());
            Class<?> main = Class.forName(mainClass, true, pcl);
            var constructor = main.getDeclaredConstructor(PluginInfo.class);
            constructor.setAccessible(true);
            Object obj = constructor.newInstance(info);
            if (!(obj instanceof Plugin plugin)) {
                pcl.close();
                throw new PluginException("Current class is not a Plugin: " + mainClass);
            }

            plugin.onLoad();
            Logger.info("[PluginManager] Loaded: " + description + " from " + jarPath.getFileName());

            LoadedPlugin lp = new LoadedPlugin(info, plugin, pcl);
            if (plugins.containsKey(info.name())) {
                pcl.close();
                throw new PluginException("A plugin with the name '" + info.name() + "' is already loaded.");
            }
            plugins.put(info.name(), lp);
        }
    }

    private static String required(Map<String, Object> m, String k) throws PluginException {
        Object v = m.get(k);
        if (v == null || v.toString().isBlank())
            throw new PluginException("Missing required field '" + k + "' in plugin.yml");
        return v.toString().trim();
    }

    private static Map<String, Object> loadYamlMap(InputStream in) throws PluginException {
        LoaderOptions options = new LoaderOptions();
        Yaml yaml = new Yaml(new SafeConstructor(options));
        Object data = yaml.load(in);
        if (data == null) return Collections.emptyMap();
        if (!(data instanceof Map<?, ?> map)) {
            throw new PluginException("plugin.yml: root must be a mapping");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<?, ?> e : map.entrySet()) {
            Object key = e.getKey();
            if (!(key instanceof String ks)) {
                throw new PluginException("plugin.yml: keys must be strings (found: " + key + ")");
            }
            result.put(ks, e.getValue());
        }
        return result;
    }

    public Collection<Plugin> getPlugins() {
        List<Plugin> list = new ArrayList<>();
        for (LoadedPlugin lp : plugins.values()) list.add(lp.instance);
        return Collections.unmodifiableList(list);
    }

    private static final class LoadedPlugin {
        final PluginInfo info;
        final Plugin instance;
        final PluginClassLoader classLoader;

        LoadedPlugin(PluginInfo info, Plugin instance, PluginClassLoader cl) {
            this.info = info;
            this.instance = instance;
            this.classLoader = cl;
        }
    }
}
