package com.griddynamics.task_1;

import com.griddynamics.task_1.util.FileUtils;
import com.griddynamics.task_1.util.SizeEstimator;
import com.griddynamics.task_1.util.SizeUnit;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.griddynamics.task_1.util.SizeUnit.UnitType.BYTE;

@Data
public class FileSplitter {
    
    private final Path file;
    
    private final SizeUnit maxTempFileSize;
    
    
    public FileSplitter(@NotNull final Path file, final SizeUnit maxTempFileSize) {
        this.file = file;
        this.maxTempFileSize = maxTempFileSize;
    }
    
    
    @SneakyThrows
    public List<Path> split() {
        List<Path> tempFiles = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            split(tempFiles, reader);
        }
        return tempFiles;
    }
    
    
    private void split(
            @NotNull final List<Path> tempFiles, @NotNull final BufferedReader reader) throws IOException {
        
        String line;
        double tempFileSize = 0;
        List<String> tempFileContent = new ArrayList<>();
        
        while ((line = reader.readLine()) != null) {
            tempFileSize = addToContentAndGetSize(tempFiles, line, tempFileSize, tempFileContent);
        }
    }
    
    
    private double addToContentAndGetSize(@NotNull final List<Path> tempFiles,
                                          @NotNull final String line,
                                          double tempFileSize,
                                          @NotNull final List<String> tempFileContent) throws IOException {
        
        tempFileSize += SizeEstimator.getSizeOfString(line, BYTE);
        tempFileSize = preventSizeOverflow(tempFiles, tempFileSize, tempFileContent);
        
        tempFileContent.add(line);
        return tempFileSize;
    }
    
    
    private double preventSizeOverflow(
            @NotNull final List<Path> tempFiles,
            double tempFileSize,
            @NotNull final List<String> tempFileContent) throws IOException {
        
        if (tempFileSize >= maxTempFileSize.getValueIn(BYTE)) {
            writeToTempFile(tempFileContent, tempFiles);
            tempFileSize = 0;
            tempFileContent.clear();
        }
        return tempFileSize;
    }
    
    
    private void writeToTempFile(@NotNull final List<String> tempFileContent,
                                 @NotNull final List<Path> tempFiles) throws IOException {
        
        Path tempFile = FileUtils.createTempFile(file.getFileName().toString());
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            for (int i = 0; i < tempFileContent.size(); i++) {
                writer.write(tempFileContent.get(i));
                if (i != tempFileContent.size() - 1) writer.newLine();
            }
        }
        tempFiles.add(tempFile);
    }
    
    
}
