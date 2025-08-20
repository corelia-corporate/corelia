package dev.corelia.core;

import dev.corelia.api.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Logger {
    public static void info(String message) {
        String caller = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("[INFO] " + caller + ": " + message);
    }

    public static void warn(String message) {
        String caller = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("[WARN] " + caller + ": " + message);
    }

    public static void error(String message) {
        String caller = Thread.currentThread().getStackTrace()[2].getClassName();
        System.err.println("[ERROR] " + caller + ": " + message);
    }

    public static void debug(String message) {
        String caller = Thread.currentThread().getStackTrace()[2].getClassName();
        System.out.println("[DEBUG] " + caller + ": " + message);
    }
}