package me.overlord.bot.commands.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import me.overlord.bot.App;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.util.Constants.UserType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONObject;

@CommandSet
public class FortniteCommands {

  @Command(value = "fnstats", permission = UserType.Administrator)
  public void hey(MessageReceivedEvent event, String[] arguments) throws Exception {

    HttpResponse<JsonNode> response =
        Unirest.get(
                "https://api.fortnitetracker.com/v1/profile/" + arguments[1] + "/" + arguments[2])
            .header(
                "TRN-Api-Key", App.properties.get("fornite.apiKey", "no-fortnite-key-configured"))
            .asJson();

    event
        .getChannel()
        .sendMessage(
            buildForniteStatsEmbed(response.getBody().getObject().getJSONObject("stats"), "test"))
        .queue();
  }

  private MessageEmbed buildForniteStatsEmbed(JSONObject stats, String playerName) {
    JSONObject root = stats.getJSONObject("lifetimestats");
    return new EmbedBuilder()
        .setTitle("Fortnite Stats For " + playerName)
        .addField("Test", root.toString(), true)
        .build();
  }
}
