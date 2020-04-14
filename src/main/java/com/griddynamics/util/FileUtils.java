package com.griddynamics.util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {
    
    private FileUtils() {
    
    }
    
    @NotNull
    public static Path createTempFile(@NotNull final String filePrefix) throws IOException {
        Path tempFile = Files.createTempFile(filePrefix, ".tmp");
        tempFile.toFile().deleteOnExit();
        return tempFile;
    }
    
}
