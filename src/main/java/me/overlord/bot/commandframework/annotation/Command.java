package me.overlord.bot.commandframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import me.overlord.bot.util.Constants;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
  String value();

  Constants.UserType permission() default Constants.UserType.Everyone;
}
