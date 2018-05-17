package me.overlord.bot.commandframework;

import static me.overlord.bot.App.commands;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.overlord.bot.App;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.util.Constants;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor {

  private static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

  public CommandExecutor(JDA jda) {
    jda.addEventListener(
        new ListenerAdapter() {
          @Override
          public void onMessageReceived(MessageReceivedEvent event) {
            handleExecution(event);
          }
        });
  }

  private void handleExecution(final MessageReceivedEvent event) {

    JDA jda = event.getJDA();

    if (event.getAuthor() == jda.getSelfUser() || event.getAuthor().isBot()) return;

    String[] splitMessage = event.getMessage().getContentRaw().split("[\\s&&[^\\n]]++");

    if (splitMessage[0].startsWith(App.properties.get("bot.commandPrefix", ";;"))) {

      String commandString = sanitizeCommandString(splitMessage[0]);
      boolean validCommand = false;

      if (commands.keySet().contains(commandString)) validCommand = true;

      if (validCommand) {
        if (canPerformAction(
            event,
            commands.get(commandString).getAnnotation(Command.class).permission().getRoleId())) {
          Thread t = new Thread(() -> invokeMethod(commandString, splitMessage, event));
          t.setDaemon(true);
          t.start();
        } else {
          event
              .getChannel()
              .sendMessage("Did you really think I'd " + "let you do that? \uD83E\uDD14")
              .queue();
        }
      } else {

        String commandMatchCandidate =
            findClosestMatch(commands.keySet(), commandString).toString();

        if (!commandMatchCandidate.isEmpty()) {
          event
              .getChannel()
              .sendMessage(
                  "I couldn't find the "
                      + "command '"
                      + commandString
                      + "' did you mean '"
                      + commandMatchCandidate
                      + "' instead?")
              .queue();
        } else {
          event
              .getChannel()
              .sendMessage(
                  "I couldn't find the " + "command " + commandString + " please try again.")
              .queue();
        }
      }
    }
  }

  /*
   * Utility methods
   */

  private void invokeMethod(String methodName, String[] arguments, MessageReceivedEvent event) {
    Method method = commands.get(methodName);
    try {
      method.setAccessible(true);
      method.invoke(method.getDeclaringClass().getConstructor().newInstance(), event, arguments);
    } catch (Exception e) {
      logger.error(
          "Error occured while invoking :: "
              + methodName
              + "\n :: Message :: "
              + e.getLocalizedMessage());
    }
  }

  private boolean canPerformAction(MessageReceivedEvent event, String permissionedRoleId) {
    if (permissionedRoleId.equalsIgnoreCase(Constants.UserType.Everyone.getRoleId())) return true;

    for (Role r : getPermissionedRoles(event.getGuild().getRoles(), permissionedRoleId)) {
      if (event.getMember().getRoles().contains(r)) return true;
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

  private static Object findClosestMatch(Collection<?> collection, Object target) {
    int distance = Integer.MAX_VALUE;
    Object closest = null;
    for (Object compareObject : collection) {
      int currentDistance =
          StringUtils.getLevenshteinDistance(compareObject.toString(), target.toString());
      if (currentDistance < distance) {
        distance = currentDistance;
        closest = compareObject;
      }
    }
    return closest;
  }

  private String sanitizeCommandString(String commandString) {
    return commandString.replaceAll(App.properties.get("bot.commandPrefix", ";;"), "");
  }
}
