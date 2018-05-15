package me.overlord.bot;

import javax.security.auth.login.LoginException;

import com.ufoscout.properlty.Properlty;
import me.overlord.bot.dsl.Command;
import me.overlord.bot.dsl.CommandBuilder;
import me.overlord.bot.dsl.CommandExecutor;
import me.overlord.bot.dsl.annotation.CommandSet;
import me.overlord.bot.listeners.MentionListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static final Properlty properties = Properlty.builder()
            .add("classpath:config/bot.properties")
            .build();

    public static void main(String[] args) throws LoginException, InterruptedException {
        logger.info("=== Loading Overlord Bot ===");

        JDA jda = new JDABuilder(AccountType.BOT)
                .setToken(properties.get("discord.token", "error-retrieving-token"))
                .buildBlocking();

        jda.addEventListener(new MentionListener());

        Reflections reflections = new Reflections("me.overlord.bot.commands");
        Set<Class<?>> commandSets = reflections.getTypesAnnotatedWith(CommandSet.class);

        CommandExecutor executor = new CommandExecutor(jda);

        for (Class<?> commandSet : commandSets) {
            try {
                final Object cs = commandSet.getConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }
}
