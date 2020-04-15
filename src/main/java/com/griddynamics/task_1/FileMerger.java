package com.griddynamics.task_1;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class FileMerger {
    
    private final List<Path> filesToMerge;
    
    private final Path outputFile;
    
    private final Comparator<Path> fileOrderComparator;
    
    public FileMerger(@NotNull final List<Path> filesToMerge,
                      @NotNull final Path outputFile,
                      @NotNull final Comparator<Path> fileOrderComparator) {
        
        this.filesToMerge = filesToMerge;
        this.outputFile = outputFile;
        this.fileOrderComparator = fileOrderComparator;
    }
    
    @SneakyThrows
    public void merge() {
        filesToMerge.sort(fileOrderComparator);
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            mergeWithWriter(writer);
        }
    }
    
    private void mergeWithWriter(@NotNull final BufferedWriter writer) throws IOException {
        for (Path path : filesToMerge) {
            writeFileContent(writer, path);
            writer.newLine();
        }
    }
    
    private void writeFileContent(
            @NotNull final BufferedWriter writer, @NotNull final Path file) throws IOException {
        
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
                
                if (line != null) {
                    writer.newLine();
                }
            }
        }
    }
    
}
