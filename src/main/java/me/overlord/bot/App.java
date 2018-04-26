package me.overlord.bot;

import javax.security.auth.login.LoginException;

import com.ufoscout.properlty.Properlty;
import me.overlord.bot.listeners.MentionListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    }
}
