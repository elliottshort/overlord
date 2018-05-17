package me.overlord.bot.commands;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.util.Common;
import me.overlord.bot.util.Constants.UserType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.MiscUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;

@CommandSet
public class UtilityCommands {

  @Command(value = "status", permission = UserType.Administrator)
  private void status(MessageReceivedEvent event, String[] arguments) {

    EmbedBuilder statusEmbed =
        new EmbedBuilder()
            .setDescription("Status")
            .setColor(new Color(0, 255, 255))
            .addField("Uptime", Common.botUptime(), true)
            .addField("Ping", Long.toString(event.getJDA().getPing()) + "ms", true);

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
