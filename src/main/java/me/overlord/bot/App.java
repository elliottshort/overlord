package me.overlord.bot;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.ufoscout.properlty.Properlty;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import me.overlord.bot.commandframework.CommandExecutor;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.listeners.MentionListener;
import me.overlord.bot.listeners.NewGuildMemberHoldingQueue;
import me.overlord.bot.util.Common;
import me.overlord.bot.util.JDAUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static Logger logger = LoggerFactory.getLogger(App.class);

  public static final Properlty properties =
      Properlty.builder().add("classpath:config/bot.properties").build();
  public static Map<String, Method> commands = new HashMap<>();

  public static Cache<String, String> holdingQueueGuildCache =
      Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

  public static void main(String[] args) throws LoginException, InterruptedException {
    logger.info("=== Loading Overlord Bot ===");

    JDA jda =
        new JDABuilder(AccountType.BOT)
            .setToken(properties.get("discord.token", "error-retrieving-token"))
            .buildBlocking();

    jda.addEventListener(new MentionListener());
    jda.addEventListener(new NewGuildMemberHoldingQueue());

    Reflections reflections =
        new Reflections(
            "me.overlord.bot",
            new MethodAnnotationsScanner(),
            new TypeAnnotationsScanner(),
            new SubTypesScanner());

    CommandExecutor executor = new CommandExecutor(jda);

    Set<Class<?>> commandSets = reflections.getTypesAnnotatedWith(CommandSet.class);
    Set<Method> commandMethods = reflections.getMethodsAnnotatedWith(Command.class);

    for (Guild g : jda.getGuilds()) {
      JDAUtils.repopulateHoldingQueue(g.getController());
    }

    for (Method command : commandMethods)
      commands.put(command.getAnnotation(Command.class).value(), command);

    logger.info("==== Number of commands loaded :: " + commands.size() + " ====");

    for (Class<?> commandSet : commandSets) {
      try {
        final Object cs = commandSet.getConstructor().newInstance();
      } catch (Exception e) {
        logger.error(e.getLocalizedMessage());
      }
    }
    Common.printCommandsWithoutHelp();
  }
}
