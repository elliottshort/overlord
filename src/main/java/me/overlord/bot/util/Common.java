package me.overlord.bot.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
}
