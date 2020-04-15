package com.griddynamics.task_1.comparator;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class BasicFileComparator implements Comparator<Path> {
    
    @Override
    @SneakyThrows
    public int compare(@NotNull final Path left, @NotNull final Path right) {
        try (BufferedReader leftReader = Files.newBufferedReader(left);
             BufferedReader rightReader = Files.newBufferedReader(right)) {
            
            String leftLine = leftReader.readLine();
            String rightLine = rightReader.readLine();
            
            if (leftLine == null) return -1;
            if (rightLine == null) return 1;
            
            return leftLine.compareTo(rightLine);
        }
    }
    
}
