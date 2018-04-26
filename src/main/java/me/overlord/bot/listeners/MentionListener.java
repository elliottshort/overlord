package me.overlord.bot.listeners;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MentionListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getAuthor().isBot())
            return;

        if (event.getMessage().getContentRaw().toLowerCase().contains(event.getJDA().getSelfUser().getName().toLowerCase())
                || event.getMessage().isMentioned(event.getJDA().getSelfUser())) {
            event.getMessage().addReaction("\uD83D\uDC40").queue();
            event.getChannel().sendMessage("I'm performing this action because " + event.getAuthor().getName() + " mentioned me.").queue();
        }
    }
}