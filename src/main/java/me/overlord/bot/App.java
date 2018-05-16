package me.overlord.bot;

import javax.security.auth.login.LoginException;

import com.ufoscout.properlty.Properlty;
import me.overlord.bot.commandframework.CommandExecutor;
import me.overlord.bot.commandframework.annotation.Command;
import me.overlord.bot.commandframework.annotation.CommandSet;
import me.overlord.bot.listeners.MentionListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;


public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static final Properlty properties = Properlty.builder()
            .add("classpath:config/bot.properties")
            .build();

    public static Map<String, Method> commands = new HashMap<>();

    public static void main(String[] args) throws LoginException, InterruptedException {
        logger.info("=== Loading Overlord Bot ===");

        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(properties.get("discord.token", "error-retrieving-token"))
                .buildBlocking();

        jda.addEventListener(new MentionListener());

        Reflections reflections = new Reflections("me.overlord.bot",
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner(),
                new SubTypesScanner());

        CommandExecutor executor = new CommandExecutor(jda);
        Set<Class<?>> commandSets = reflections.getTypesAnnotatedWith(CommandSet.class);
        Set<Method> commandMethods = reflections.getMethodsAnnotatedWith(Command.class);

        for (Method command : commandMethods)
            commands.put(command.getAnnotation(Command.class).value(), command);

        System.out.println("Command length is :: " + commands.size());

        for (Class<?> commandSet : commandSets) {
            try {
                final Object cs = commandSet.getConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }
}
