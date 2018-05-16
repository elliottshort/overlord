package me.overlord.bot.commands;


import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.util.Constants.UserType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandSet
public class ModerationCommands {

    @Command(value = "heyThere", permission = UserType.Administrator)
    public void hey(MessageReceivedEvent event, String[] arguments) {
        System.out.println("Hello there!");
        event.getChannel().sendMessage("Hello there! " + arguments[1]).queue();
    }

    @Command(value = "everyone", permission = UserType.Everyone)
    public void doSomething(MessageReceivedEvent event, String[] arguments) {
        event.getChannel().sendMessage("A command everyone can use regardless of their role!").queue();
    }

    @Command(value = "someCommand", permission = UserType.Administrator)
    public void someCommand(MessageReceivedEvent event, String[] arguments) {
        event.getChannel().sendMessage("This is another command for administrators!").queue();
    }
}

