package me.overlord.bot.listeners;

import me.overlord.bot.App;
import me.overlord.bot.util.JDAUtils;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {
  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    String memberRoleName = App.properties.get("member.roleName", "Member");
    if (!JDAUtils.guildHasRole(event.getGuild(), memberRoleName)) {
      JDAUtils.createMemberRole(event.getGuild(), memberRoleName);
    }
  }
}
