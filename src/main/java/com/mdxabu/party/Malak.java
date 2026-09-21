package com.mdxabu.party;

import com.mdxabu.commands.InMessageCommands;
import com.mdxabu.commands.SlashCommands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;

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
                .addEventListeners(new SlashCommands())
                .setActivity(Activity.watching("Thug Life"))
                .setStatus(OnlineStatus.ONLINE)
                .build();



        CommandListUpdateAction commands =  MalakBuilder.updateCommands();

        commands
                .addCommands(
                        slash("hello", "Say hello to Malak"),

                        Commands.slash("rps", "Rock Paper Scissor Shoot!").addOptions(
                                new OptionData(STRING, "choice", "Enter your choice", true)
                                        .addChoice("Rock", "rock")
                                        .addChoice("Paper", "paper")
                                        .addChoice("Scissors", "scissors")
                        )

                )
                .queue(
                        success ->
                                logger.info(
                                        "Successfully registered {} slash commands!",
                                        success.size()
                                ),
                        failure ->
                                logger.error(
                                        "Failed to register slash commands: {}",
                                        failure.getMessage()
                                )
                );





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
