package io.barrowisp.craftastrophe.defines;

import org.intellij.lang.annotations.Pattern;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * <p>Mod item names are used as registry and unlocalized names.</p>
 * They should not be empty or contain whitespaces
 */
@Retention(RetentionPolicy.CLASS)
@Target({ FIELD, PARAMETER, LOCAL_VARIABLE})

@Pattern("^[\\w]+$")
public @interface ModItemName {}
