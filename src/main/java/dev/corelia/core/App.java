package dev.corelia.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.nio.file.Path;

public final class App {
    public static void main(String[] args) throws Exception {
        Path pluginsDir = Path.of("plugins");
        PluginManager pm = new PluginManager(pluginsDir);

        pm.loadAll();

        String DISCORD_BOT_TOKEN = System.getenv("DISCORD_BOT_TOKEN");

        if (DISCORD_BOT_TOKEN == null || DISCORD_BOT_TOKEN.isEmpty()) {
            throw new IllegalStateException("DISCORD_BOT_TOKEN environment variable is not set.");
        }

        JDA jda = JDABuilder.createDefault(DISCORD_BOT_TOKEN)
                .build();

        jda.awaitReady();
    }
}