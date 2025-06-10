package org.example.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a servlet requires user authentication and, optionally, specific roles.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) // Can be applied to classes
public @interface AuthRequired {
    /**
     * An array of role names that are allowed to access the servlet.
     * If empty, any authenticated user is allowed.
     */
    String[] roles() default {};
} 