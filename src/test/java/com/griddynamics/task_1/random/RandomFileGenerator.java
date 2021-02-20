package com.griddynamics.task_1.random;

import com.griddynamics.task_1.util.SizeUnit;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.griddynamics.task_1.util.SizeEstimator.getSizeOfFile;
import static com.griddynamics.task_1.util.SizeEstimator.getSizeOfString;
import static com.griddynamics.task_1.util.SizeUnit.UnitType.KB;
import static java.nio.file.Files.newBufferedWriter;

@Data
public class RandomFileGenerator {
    
    private static final int FILE_NAME_LENGTH = 7;
    
    private static final String FILE_NAME_PATTERN = "[A-Za-z0-9_-]+";
    
    private final SizeUnit fileSizeLimit;
    
    private final RandomStringGenerator fileNameGenerator;
    
    public RandomFileGenerator(SizeUnit fileSizeLimit) {
        
        this.fileSizeLimit = fileSizeLimit;
        this.fileNameGenerator = new RandomStringGenerator(FILE_NAME_LENGTH, FILE_NAME_LENGTH + 1);
    }
    
    @SneakyThrows
    public Path createRandomFile(RandomStringGenerator fileLinesGenerator) {
        
        Path tempFile = Files.createTempFile(fileNameGenerator.generate(FILE_NAME_PATTERN), ".txt");
        double fileSizeLimitInKB = fileSizeLimit.getValueIn(KB);
        
        populateFile(fileLinesGenerator, tempFile, fileSizeLimitInKB);
        tempFile.toFile().deleteOnExit();
        return tempFile;
    }
    
    private void populateFile(RandomStringGenerator fileLinesGenerator, Path tempFile, double fileSizeLimitInKB)
            throws IOException {
        
        try (final BufferedWriter writer = newBufferedWriter(tempFile)) {
            double sizeOfFileInKB;
            while (((sizeOfFileInKB = getSizeOfFile(tempFile, KB)) < fileSizeLimitInKB)) {
                if (!generateAndWriteLine(fileLinesGenerator, writer, sizeOfFileInKB, fileSizeLimitInKB)) {
                    break;
                }
            }
        }
    }
    
    private boolean generateAndWriteLine(RandomStringGenerator fileLinesGenerator,
                                         BufferedWriter writer,
                                         double sizeOfFileInKB,
                                         double fileSizeLimitInKB
    ) throws IOException {
        
        String line = fileLinesGenerator.generate(FILE_NAME_PATTERN);
        sizeOfFileInKB += getSizeOfString(line, KB);
        if (sizeOfFileInKB > fileSizeLimitInKB) {
            return false;
        }
        writer.write(line);
        writer.newLine();
        return true;
    }
    
}
