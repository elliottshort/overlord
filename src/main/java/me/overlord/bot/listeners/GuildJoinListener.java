package me.overlord.bot.listeners;

import me.overlord.bot.App;
import me.overlord.bot.util.JDAUtils;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildJoinListener extends ListenerAdapter {
  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    String holdingRoleName = App.properties.get("holding.roleName", "Holding");
    if (!JDAUtils.guildHasRole(event.getGuild(), holdingRoleName)) {
      JDAUtils.createHoldingRole(event.getGuild(), holdingRoleName);
    }
  }
}
