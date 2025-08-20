package dev.corelia.api;

public class PluginException extends Exception {
    public PluginException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public PluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
