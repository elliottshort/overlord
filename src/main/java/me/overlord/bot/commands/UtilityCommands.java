package me.overlord.bot.commands;

import java.awt.*;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.util.Common;
import me.overlord.bot.util.Constants.UserType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandSet
public class UtilityCommands {

  @Command(value = "status", permission = UserType.Administrator)
  private void status(MessageReceivedEvent event, String[] arguments) {

    EmbedBuilder statusEmbed =
        new EmbedBuilder()
            .setColor(new Color(0, 255, 255))
            .addField("Uptime", Common.botUptime(), true)
            .addField("Latency", Long.toString(event.getJDA().getPing()) + "ms", true);

    event.getChannel().sendMessage(statusEmbed.build()).queue();
  }

  @Command(value = "plot-joins", permission = UserType.Administrator)
  private void plot(MessageReceivedEvent event, String[] arguments) {
    event.getChannel().sendTyping().queue();
    event
        .getChannel()
        .sendFile(Common.drawPlot(event.getGuild(), event.getMessage().getCreationTime()))
        .queue();
  }
}
