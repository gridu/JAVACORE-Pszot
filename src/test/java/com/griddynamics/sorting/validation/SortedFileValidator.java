package com.griddynamics.sorting.validation;

import io.qameta.allure.Step;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Data
public class SortedFileValidator {
    
    private final Path file;
    
    private final Comparator<String> comparator;
    
    public SortedFileValidator(@NotNull final Path file, @NotNull final Comparator<String> comparator) {
        this.file = file;
        this.comparator = comparator;
    }
    
    @SneakyThrows
    @Step("Assert lines are sorted")
    public boolean validate() {
        try (final BufferedReader reader = Files.newBufferedReader(file)) {
            String left = reader.readLine();
            String right = reader.readLine();
            
            //If is end of file, then there is no reason to continue comparing lines (It is sorted properly)
            if (left == null || right == null) {
                return true;
            }
            
            if (comparator.compare(left, right) > 0) {
                return false;
            }
        }
        return true;
    }
    
}
