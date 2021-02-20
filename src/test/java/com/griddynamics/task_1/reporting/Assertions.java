package com.griddynamics.task_1.reporting;

import com.griddynamics.task_1.sorting.validation.SortedFileValidator;
import com.griddynamics.task_1.util.SizeUnit;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class Assertions {
    
    private static final Logger LOGGER = Logger.getLogger(Assertions.class.getName());
    
    public static void assertSortedFile(Path file, SortedFileValidator fileValidator) {
    
        LOGGER.log(Level.INFO, "Assert file " + file.getFileName() + " is sorted properly");
        assertThat(fileValidator.validate(), Matchers.equalTo(true));
    }
    
    public static void assertSplitFiles(SizeUnit sizeUnitOfFile, SizeUnit maxTempFileSizeUnit, List<Path> splitFiles) {
        
        double sizeOfFile = sizeUnitOfFile.getValueIn(maxTempFileSizeUnit.getUnit());
        int estimatedAmountOfSplitFiles = estimateAmountOfSplitFiles(sizeOfFile, maxTempFileSizeUnit.getValue());
        assertSplitFileAmount(splitFiles.size(), estimatedAmountOfSplitFiles);
    }
    
    public static int estimateAmountOfSplitFiles(final double sizeOfFile, final double maxTempFileSize) {
    
        return (int) Math.ceil(sizeOfFile / maxTempFileSize);
    }
    
    public static void assertSplitFileAmount(final int actualSplitFilesAmount, final int estimatedAmountOfSplitFiles) {
        
        assertThat(actualSplitFilesAmount, lessThan(estimatedAmountOfSplitFiles));
    }
    
    @SneakyThrows
    public static void assertMergedFiles(List<Path> filesToMerge, Path outputFile
    ) {
        
        try (BufferedReader mainReader = Files.newBufferedReader(outputFile)) {
            for (Path file : filesToMerge) {
                assertMergedFile(mainReader, file);
            }
        }
        
    }
    
    private static void assertMergedFile(BufferedReader mainReader, Path file) throws IOException {
        
        String mainLine;
        try (BufferedReader secondaryReader = Files.newBufferedReader(file)) {
            String secondaryLine;
            while ((secondaryLine = secondaryReader.readLine()) != null) {
                mainLine = mainReader.readLine();
                assertThat(mainLine, equalTo(secondaryLine));
            }
        }
    }
    
}
