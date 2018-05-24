package me.overlord.bot.listeners;

import me.overlord.bot.App;
import me.overlord.bot.util.JDAUtils;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewGuildMemberHoldingQueue extends ListenerAdapter {

  private static Logger logger = LoggerFactory.getLogger(NewGuildMemberHoldingQueue.class);

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {

    event
        .getGuild()
        .getController()
        .addRolesToMember(
            event.getMember(),
            event
                .getGuild()
                .getRolesByName(App.properties.get("holding.roleName", "Holding"), true))
        .queue();

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

      // if (event.getAuthor().isBot()) return;

      if (App.holdingQueueGuildCache.getIfPresent(event.getAuthor().getId()) != null) {

        GuildController guildController =
            event
                .getJDA()
                .getGuildById(App.holdingQueueGuildCache.getIfPresent(event.getAuthor().getId()))
                .getController();

        handleRoles(guildController, event);

        event
            .getChannel()
            .sendMessage(
                "***Success***: Channels and members are now visible to you on the server.")
            .queue();
      }
    } else {
      logger.error(
          "Error :: No holding queue cache entry found for: \nName: "
              + event.getAuthor().getName()
              + "\nID: "
              + event.getAuthor().getId());
    }
  }

  private void handleRoles(GuildController guildController, PrivateMessageReceivedEvent event) {
    guildController
        .modifyMemberRoles(
            guildController.getGuild().getMemberById(event.getAuthor().getId()),
            guildController
                .getGuild()
                .getRolesByName(App.properties.get("member.roleName", "Member"), true),
            guildController
                .getGuild()
                .getRolesByName(App.properties.get("holding.roleName", "Holding"), true))
        .complete();
    App.holdingQueueGuildCache.invalidate(event.getAuthor().getId());
  }
}
