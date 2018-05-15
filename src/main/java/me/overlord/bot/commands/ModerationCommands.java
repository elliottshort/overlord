package me.overlord.bot.commands;


import me.overlord.bot.dsl.CommandBuilder;
import me.overlord.bot.dsl.annotation.CommandSet;

@CommandSet
public class ModerationCommands {
    static {
        new CommandBuilder()
                .when("hello")
                .respond("Goodbye")
                .build();
    }
}