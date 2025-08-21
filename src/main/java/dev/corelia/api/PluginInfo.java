package dev.corelia.api;

import java.util.Objects;

public final class PluginInfo {
    private final String name;
    private final String version;
    private final String description;
    private final String mainClass;

    public PluginInfo(String name, String version, String description, String mainClass) {
        this.name = Objects.requireNonNull(name);
        this.version = Objects.requireNonNull(version);
        this.description = Objects.requireNonNull(description);
        this.mainClass = Objects.requireNonNull(mainClass);
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getMainClass() {
        return mainClass;
    }

    @Override
    public String toString() {
        return name + " v" + version + " (" + mainClass + ")";
    }
}