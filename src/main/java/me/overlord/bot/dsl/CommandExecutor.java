package me.overlord.bot.dsl;


import me.overlord.bot.App;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    public static List<Command> commands = new ArrayList<>();

    public CommandExecutor(JDA jda) {
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                handleMessage(event);
            }
        });
    }

    private void handleMessage(final MessageReceivedEvent event) {
        JDA jda = event.getJDA();

        if (event.getAuthor() == jda.getSelfUser()) {
            return;
        }

        String[] splitMessage = event.getMessage().getContentRaw().split("[\\s&&[^\\n]]++");
        String commandString = App.properties.get("bot.commandPrefix", ";;") + splitMessage[0];

        for (Command command : commands) {
            if (commandString.equalsIgnoreCase((command.getCommand()))) {
                event.getChannel().sendMessage((String) command.getResponse()).queue();
            }
        }
    }
}
