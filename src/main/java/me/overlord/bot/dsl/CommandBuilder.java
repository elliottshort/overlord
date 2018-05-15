package me.overlord.bot.dsl;

public class CommandBuilder {

    private Command command;

    public CommandBuilder() {
        command = new Command();
    }

    public CommandBuilder when(String commandName) {
        command.setCommand(commandName);
        return this;
    }

    public CommandBuilder respond(String s) {
        command.setResponse(s);
        return this;
    }

    public Command build() {
        CommandExecutor.commands.add(command);
        return command;
    }
}


