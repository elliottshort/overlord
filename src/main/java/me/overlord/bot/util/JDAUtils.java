package me.overlord.bot.util;

import java.awt.Color;
import me.overlord.bot.App;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDAUtils {

  private static Logger logger = LoggerFactory.getLogger(JDAUtils.class);

  public static void createMemberRole(Guild guild, String memberRoleName) {
    if (!guildHasRole(guild, memberRoleName)) {
      logger.warn(
          "** Guild Does NOT Have the Member role setup, attempting to create.."
              + "\n If I fail to create the role, please give me administrative priveleges and try again. ");
      handleRoleCreation(guild, memberRoleName);

      /*
       * TODO: Figure out a proper way to programatically set the role permissioning. Here Don't use the
       * channel-level permission override hack.
       */

    }
  }

  public static void handleRoleCreation(Guild guild, String roleName) {
    guild.getController().createRole().setName(roleName).complete();
  }

  public static boolean guildHasRole(Guild guild, String roleName) {
    return guild
        .getRoles()
        .stream()
        .anyMatch(role -> role.getName().toLowerCase().equals(roleName.toLowerCase()));
  }

  public static Role getRoleByName(Guild guild, String roleName) {
    return guild
        .getRoles()
        .stream()
        .filter(role -> role.getName().toLowerCase().equals(roleName.toLowerCase()))
        .findFirst()
        .orElse(null);
  }

  public static EmbedBuilder buildRulesEmbed(String guildName) {
    return new EmbedBuilder()
        .setColor(new Color(229, 132, 252))
        .setTitle(guildName + " Server Rules")
        .addBlankField(false)
        .setThumbnail("https://image.ibb.co/cY7nwT/information.png")
        .addField(
            "1. Do not advertise",
            "This includes other servers, products or services that you stand to benefit from financially, your youtube channel or the like.",
            false)
        .addField(
            "2. Do not be a dick",
            "If you make yourself particularly hard to deal with, if you pretend that you are better than anyone else, or if you are just in general very annoying and not a good fit for this server we will remove you. Permanently.",
            false)
        .addField(
            "3. Speak English only in the chat",
            "This speaks for itself, we do not expect staff to speak extra languages and we cannot moderate what we do not understand. Sorry.",
            false)
        .addField(
            "4. No NSFW content in any capacity",
            "Always ask yourself \"Would my boss be okay with this fullscreen on my monitor as he walked by?\". If the answer is no, don't post it. The content here is meant to be pg13. Not pg15, not pg18 - if you wouldn't show it to your kid brother in front of your mother, it has no place here. No hentai, no porn, no anything like that.",
            true)
        .addBlankField(false)
        .setFooter(
            "In order to gain access to the server, please type: "
                + App.properties.get("bot.commandPrefix", ";;")
                + "accept - by doing so, you agree to abide by the rules and understand that failure to do so will result in permanent removal from the server.",
            null);
  }
}
