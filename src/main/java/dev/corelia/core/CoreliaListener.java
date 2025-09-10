package dev.corelia.core;

import dev.corelia.api.events.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CoreliaListener extends ListenerAdapter {
    private final PluginEventBus eventBus;

    public CoreliaListener(PluginEventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onMessageReceived(net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        eventBus.dispatch(new dev.corelia.api.events.MessageReceivedEvent(event));
    }
}
