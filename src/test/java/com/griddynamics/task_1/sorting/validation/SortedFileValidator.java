package com.griddynamics.task_1.sorting.validation;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Data
public class SortedFileValidator {
    
    private final Path file;
    
    private final Comparator<String> comparator;
    
    public SortedFileValidator(Path file, Comparator<String> comparator) {
        
        this.file = file;
        this.comparator = comparator;
    }
    
    @SneakyThrows
    public boolean validate() {
        
        try (final BufferedReader reader = Files.newBufferedReader(file)) {
            String left = reader.readLine();
            String right = reader.readLine();
            
            //If end of file is reached, then there is no reason to continue comparing lines (It is sorted properly)
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
