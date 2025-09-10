package dev.corelia.api.events;

import lombok.Getter;

@Getter
public class MessageReceivedEvent implements PluginEvent {
    private final String content;
    private final long authorId;
    private final String authorName;
    private final long channelId;

    public MessageReceivedEvent(net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        this.content = event.getMessage().getContentRaw();
        this.authorId = event.getAuthor().getIdLong();
        this.authorName = event.getAuthor().getName();
        this.channelId = event.getChannel().getIdLong();
    }
}
