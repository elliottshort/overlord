package me.overlord.bot.commandframework;

import me.overlord.bot.App;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.util.Constants;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static me.overlord.bot.App.commands;

public class CommandExecutor {

    private static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    public CommandExecutor(JDA jda) {
        jda.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(MessageReceivedEvent event) {
                handleExecution(event);
            }
        });
    }

    private void handleExecution(final MessageReceivedEvent event) {

        JDA jda = event.getJDA();

        if (event.getAuthor() == jda.getSelfUser() || event.getAuthor().isBot())
            return;

        String[] splitMessage = event.getMessage().getContentRaw().split("[\\s&&[^\\n]]++");
        String commandString = splitMessage[0];

        for (String key : commands.keySet()) {
            if (commandString.equalsIgnoreCase((App.properties.get("bot.commandPrefix", ";;") + key))) {
                if (canPerformAction(event, commands.get(key).getAnnotation(Command.class).permission().getRoleId())) {
                    try {
                        commands.get(key).invoke(commands.get(key).getDeclaringClass().getConstructor().newInstance(),
                                event, splitMessage);
                    } catch (Exception e) {
                        logger.error("Error :: " + e.getLocalizedMessage());
                    }
                } else {
                    event.getChannel().sendMessage("Did you really think I'd let you do that? \uD83E\uDD14").queue();
                }
            }
        }
    }

    private boolean canPerformAction(MessageReceivedEvent event, String permissionedRoleId) {

        if (permissionedRoleId.equalsIgnoreCase(Constants.UserType.Everyone.getRoleId()))
            return true;

        for (Role r : getPermissionedRoles(event.getGuild().getRoles(), permissionedRoleId)) {
            if (event.getMember().getRoles().contains(r))
                return true;
        }
        return false;
    }

    private List<Role> getPermissionedRoles(List<Role> serverRoles, String permissionedRoleId) {
        List<Role> permissionedRoles = new ArrayList<Role>();
        for (int i = 0; i < serverRoles.size(); i++) {
            if (serverRoles.get(i).getId().equals(permissionedRoleId)) {
                permissionedRoles = serverRoles.subList(0, i + 1);
            }
        }
        return permissionedRoles;
    }
}
