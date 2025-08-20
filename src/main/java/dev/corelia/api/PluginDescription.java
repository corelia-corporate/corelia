package dev.corelia.api;

import java.util.Objects;

public final class PluginDescription {
    private final String name;
    private final String version;
    private final String mainClass;

    public PluginDescription(String name, String version, String mainClass) {
        this.name = Objects.requireNonNull(name);
        this.version = Objects.requireNonNull(version);
        this.mainClass = Objects.requireNonNull(mainClass);
    }

    public String name() {
        return name;
    }

    public String version() {
        return version;
    }

    public String mainClass() {
        return mainClass;
    }

    @Override
    public String toString() {
        return name + " v" + version + " (" + mainClass + ")";
    }
}