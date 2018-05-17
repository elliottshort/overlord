package me.overlord.bot.util;

import me.overlord.bot.App;

public class Constants {

  public enum ArgumentType {
    Sentence,
    Number,
    String,
    Channel,
    User
  }

  public enum UserType {
    Administrator(Const.ADMINISTRATOR_VALUE),
    Moderator(Const.MODERATOR_VALUE),
    Everyone(Const.EVERYONE_VALUE);

    private final String roleId;

    UserType(String roleId) {
      this.roleId = roleId;
    }

    public String getRoleId() {
      return roleId;
    }

    public static class Const {
      static final String ADMINISTRATOR_VALUE = App.properties.get("role.admin", "insert-role-id");
      static final String MODERATOR_VALUE = App.properties.get("role.moderator", "insert-role-id");
      static final String EVERYONE_VALUE = "EVERYONE";
    }
  }
}
