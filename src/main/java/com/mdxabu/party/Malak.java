package com.mdxabu.party;

import com.mdxabu.commands.InMessageCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

public class Malak extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Malak.class);

    static JDA MalakBuilder;

    public static void run(){
        String TOKEN = System.getenv("TOKEN");
        if (TOKEN == null || TOKEN.isBlank()) {
            logger.error("TOKEN environment variable is not set!");
            System.exit(1);
        }

        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
        );

        MalakBuilder = JDABuilder.createDefault(TOKEN,intents)
                .addEventListeners(new Malak())
                .addEventListeners(new InMessageCommands())
                .setActivity(Activity.watching("Thug Life"))
                .setStatus(OnlineStatus.ONLINE)
                .build();





        // --- Graceful Shutdown Hook ---
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    logger.info("Shutting down Kira bot...");
                    if (MalakBuilder != null) {
                        MalakBuilder.shutdown();
                        logger.info("JDA shutdown complete.");
                    }
                })
        );

        logger.info("Malak bot is starting up!");
    }
}
