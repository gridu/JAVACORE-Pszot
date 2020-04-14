package com.griddynamics;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class Main {
    
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    
    
    public static void main(@NotNull final String[] args) {
        LOGGER.log(INFO, "Hello World!");
    }
    
}
