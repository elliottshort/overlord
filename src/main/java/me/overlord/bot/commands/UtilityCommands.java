package me.overlord.bot.commands;

import java.awt.Color;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.util.Constants.UserType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

@CommandSet
public class UtilityCommands {

  @Command(value = "status", permission = UserType.Administrator)
  private void status(MessageReceivedEvent event, String[] arguments) {

    EmbedBuilder statusEmbed =
        new EmbedBuilder()
            .setDescription("Status")
            .setColor(new Color(0, 255, 255))
            .addField("Uptime", botUptime(), true)
            .addField("Ping", Long.toString(event.getJDA().getPing()) + "ms", true);

    event.getChannel().sendMessage(statusEmbed.build()).queue();
  }

  /*
   * Utility Methods
   */

  private String botUptime() {
    RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
    long seconds = rb.getUptime() / 1000;
    long d = (long) Math.floor(seconds / 86400);
    long h = (long) Math.floor((seconds % 86400) / 3600);
    long m = (long) Math.floor(((seconds % 86400) % 3600) / 60);
    long s = (long) Math.floor(((seconds % 86400) % 3600) % 60);

    if (d > 0) {
      return String.format("%sd %sh %sm %ss", d, h, m, s);
    }

    if (h > 0) {
      return String.format("%sh %sm %ss", h, m, s);
    }

    if (m > 0) {
      return String.format("%sm %ss", m, s);
    }
    return String.format("%ss", s);
  }
}
