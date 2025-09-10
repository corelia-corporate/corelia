package dev.corelia.core;

import dev.corelia.api.Plugin;
import dev.corelia.api.events.EventHandler;
import dev.corelia.api.events.PluginEvent;

import java.lang.reflect.Method;
import java.util.*;

public class PluginEventBus {
    private final Map<Class<?>, List<RegisteredHandler>> handlers = new HashMap<>();

    public void registerPlugin(Plugin plugin) {
        for (Method method : plugin.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length != 1 || !PluginEvent.class.isAssignableFrom(params[0])) {
                    throw new IllegalArgumentException(
                            "@EventHandler method must have exactly one parameter of type PluginEvent"
                    );
                }
                method.setAccessible(true);
                handlers
                        .computeIfAbsent(params[0], k -> new ArrayList<>())
                        .add(new RegisteredHandler(plugin, method));
            }
        }
    }

    public void dispatch(PluginEvent event) {
        List<RegisteredHandler> list = handlers.get(event.getClass());
        if (list == null) return;

        for (RegisteredHandler rh : list) {
            try {
                rh.method.invoke(rh.plugin, event);
            } catch (Exception e) {
                System.err.println("Erreur lors de l’appel d’un event handler: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private record RegisteredHandler(Plugin plugin, Method method) {}
}
