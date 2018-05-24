package me.overlord.bot.listeners;

import me.overlord.bot.App;
import me.overlord.bot.util.JDAUtils;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuildMemberHoldingQueue extends ListenerAdapter {

  private static Logger logger = LoggerFactory.getLogger(GuildMemberHoldingQueue.class);

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {

    event
        .getUser()
        .openPrivateChannel()
        .queue(
            (channel) -> {
              channel
                  .sendMessage(JDAUtils.buildRulesEmbed(event.getGuild().getName()).build())
                  .queue();
            });

    App.holdingQueueGuildCache.put(event.getUser().getId(), event.getGuild().getId());
  }

  @Override
  public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
    if (event
        .getMessage()
        .getContentRaw()
        .contains(App.properties.get("bot.commandPrefix", ";;") + "accept")) {

      GuildController guildController =
          event
              .getJDA()
              .getGuildById(App.holdingQueueGuildCache.getIfPresent(event.getAuthor().getId()))
              .getController();

      guildController
          .addRolesToMember(
              guildController.getGuild().getMemberById(event.getAuthor().getId()),
              guildController
                  .getGuild()
                  .getRolesByName(App.properties.get("member.roleName", "Member"), true))
          .queue();
    }
  }
}
