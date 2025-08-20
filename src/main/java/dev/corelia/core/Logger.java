package dev.corelia.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logger {
    private static final Map<ClassLoader, String> PLUGIN_BY_LOADER = new ConcurrentHashMap<>();

    public static void registerPluginClassLoader(ClassLoader cl, String pluginName) {
        if (cl != null && pluginName != null && !pluginName.isBlank()) {
            PLUGIN_BY_LOADER.put(cl, pluginName);
        }
    }

    public static void unregisterPluginClassLoader(ClassLoader cl) {
        if (cl != null) {
            PLUGIN_BY_LOADER.remove(cl);
        }
    }

    private static Class<?> resolveCaller() {
        return java.lang.StackWalker.getInstance(java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s
                        .filter(f -> f.getDeclaringClass() != Logger.class)
                        .findFirst()
                        .map(java.lang.StackWalker.StackFrame::getDeclaringClass)
                        .orElse(null));
    }

    private static void log(String level, String message, boolean error) {
        Class<?> caller = resolveCaller();
        String tag;
        if (caller == null) {
            tag = "[unknown]";
        } else {
            String plugin = PLUGIN_BY_LOADER.get(caller.getClassLoader());
            tag = (plugin != null) ? ("[Plugin][" + plugin + "]") : ("[" + caller.getSimpleName() + "]");
        }
        String line = level + tag + " " + message;
        if (error) System.err.println(line); else System.out.println(line);
    }

    public static void info(String message) { log("[INFO]", message, false); }
    public static void warn(String message) { log("[WARN]", message, false); }
    public static void error(String message) { log("[ERROR]", message, true); }
    public static void debug(String message) { log("[DEBUG]", message, false); }
}