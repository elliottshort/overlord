package me.overlord.bot.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.utils.MiscUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static me.overlord.bot.App.commands;

public class Common {

  private static Logger logger = LoggerFactory.getLogger(Common.class);

  public static void printCommandsWithoutHelp() {
    URL url = Resources.getResource("commandframework/help.json");
    StringBuilder sb = new StringBuilder();
    List<String> commandsWithoutHelp = new ArrayList<>(commands.keySet());

    try {
      String rawJson = Resources.toString(url, Charsets.UTF_8);
      JSONArray help = new JSONArray(rawJson);

      for (int i = 0; i < help.length(); i++) {
        JSONObject j = help.getJSONObject(i);
        commandsWithoutHelp.remove(j.getString("command"));
      }
    } catch (IOException e) {
      logger.error("Error checking help.json :: " + e.getLocalizedMessage());
    }

    if (!commandsWithoutHelp.isEmpty()) {
      for (String command : commandsWithoutHelp)
        logger.warn(
            "*** Help missing for command :: '"
                + command
                + "' :: please add it to the help.json file as soon as possible ***");
    }
  }

  public static String botUptime() {
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

  public static File drawPlot(Guild guild, OffsetDateTime now) {
    long start = MiscUtil.getCreationTime(guild.getIdLong()).toEpochSecond();
    long end = now.toEpochSecond();
    int width = 1000;
    int height = 600;
    List<Member> joins = new ArrayList<>(guild.getMembers());
    Collections.sort(joins, (Member a, Member b) -> a.getJoinDate().compareTo(b.getJoinDate()));
    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
    Graphics2D g2d = bi.createGraphics();
    g2d.setComposite(AlphaComposite.SrcOver);
    g2d.setColor(new Color(39, 51, 71));
    g2d.fillRect(0, 0, width, height);
    double lastX = 0;
    int lastY = height;

    for (int i = 0; i < joins.size(); i++) {
      double x = (((joins.get(i).getJoinDate()).toEpochSecond() - start) * width) / (end - start);
      int y = height - ((i * height) / joins.size());
      double angle =
          (x == lastX) ? 1.0 : Math.tan((double) (lastY - y) / (x - lastX)) / (Math.PI / 2);
      g2d.setColor(Color.getHSBColor((float) angle / 4, 1.0f, 1.0f));
      g2d.drawLine((int) x, y, (int) lastX, lastY);
      lastX = x;
      lastY = y;
    }
    g2d.setFont(g2d.getFont().deriveFont(24f));
    g2d.setColor(Color.WHITE);
    g2d.drawString("0 - " + joins.size() + " Users", 20, 26);
    g2d.drawString(
        MiscUtil.getCreationTime(guild.getIdLong()).format(DateTimeFormatter.RFC_1123_DATE_TIME),
        20,
        60);
    g2d.drawString(now.format(DateTimeFormatter.RFC_1123_DATE_TIME), 20, 90);
    File joinPlot = new File("plot.png");
    try {
      ImageIO.write(bi, "png", joinPlot);
    } catch (IOException ex) {
      logger.error("Error occured generating the plot :: " + ex.getLocalizedMessage());
    }
    return joinPlot;
  }
}
