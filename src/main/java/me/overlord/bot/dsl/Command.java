package me.overlord.bot.dsl;

import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Command extends ListenerAdapter {

    private String command;
    private String response;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getResponse() {
        return response;
    }
}

