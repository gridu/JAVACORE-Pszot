package com.griddynamics.task_1.sorting;

import com.griddynamics.task_1.config.Configuration;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

import static com.griddynamics.task_1.util.SizeEstimator.ensureAdmissibleFileSize;
import static com.griddynamics.task_1.util.SortingUtils.sortStringListInParallel;

@Data
public class BasicFileSorter implements FileSorter {
    
    private static final Configuration config = Configuration.getInstance();
    
    
    private final Comparator<String> lineComparator;
    
    public BasicFileSorter(@NotNull final Comparator<String> lineComparator) {
        this.lineComparator = lineComparator;
    }
    
    @SneakyThrows
    public void sort(@NotNull final Path file) {
        ensureAdmissibleFileSize(file, config.getMaxFileSizeUntilSplit());
        
        final List<String> lines = Files.readAllLines(file);
        final List<String> sortedLines = sortStringListInParallel(lines, lineComparator);
        Files.write(file, sortedLines, StandardOpenOption.WRITE);
    }
    
}
