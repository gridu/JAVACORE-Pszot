package com.griddynamics.task_1.reporting;

import io.qameta.allure.Attachment;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AllureAttachments {
    
    @NotNull
    @Attachment(value = "fileToSort", type = "text/plain")
    @SneakyThrows
    public static String attachFileToSort(@NotNull final Path file) {
        final StringBuilder builder = new StringBuilder();
        populateBuilder(file, builder);
        return builder.toString();
    }
    
    @NotNull
    @Attachment(value = "sortedFile", type = "text/plain")
    @SneakyThrows
    public static String attachSortedFile(@NotNull final Path file) {
        final StringBuilder builder = new StringBuilder();
        populateBuilder(file, builder);
        return builder.toString();
    }
    
    private static void populateBuilder(
            @NotNull final Path file, @NotNull final StringBuilder builder) throws IOException {
        
        try (final BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        }
    }
    
}
