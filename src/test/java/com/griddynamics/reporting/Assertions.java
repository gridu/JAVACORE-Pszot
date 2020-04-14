package com.griddynamics.reporting;

import com.griddynamics.sorting.validation.SortedFileValidator;
import com.griddynamics.util.SizeUnit;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;

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
    
    @Step("Assert file is sorted properly")
    public static void assertSortedFile(
            @NotNull final Path file, @NotNull final SortedFileValidator fileValidator) {
        
        LOGGER.log(Level.INFO, "Assert file " + file.getFileName() + " is sorted properly");
        assertThat(fileValidator.validate(), Matchers.equalTo(true));
    }
    
    @Step("Assert the file has been split correctly")
    public static void assertSplitFiles(@NotNull final SizeUnit sizeUnitOfFile,
                                        @NotNull final SizeUnit maxTempFileSizeUnit,
                                        @NotNull final List<Path> splitFiles) {
        
        double sizeOfFile = sizeUnitOfFile.getValueIn(maxTempFileSizeUnit.getUnit());
        int estimatedAmountOfSplitFiles = estimateAmountOfSplitFiles(sizeOfFile, maxTempFileSizeUnit.getValue());
        assertSplitFileAmount(splitFiles.size(), estimatedAmountOfSplitFiles);
    }
    
    @Step("Estimate amount of split files")
    public static int estimateAmountOfSplitFiles(final double sizeOfFile, final double maxTempFileSize) {
        return (int) Math.ceil(sizeOfFile / maxTempFileSize);
    }
    
    @Step("Assert the correct amount of split files")
    public static void assertSplitFileAmount(
            final int actualSplitFilesAmount, final int estimatedAmountOfSplitFiles) {
        
        assertThat(actualSplitFilesAmount, lessThan(estimatedAmountOfSplitFiles));
    }
    
    @SneakyThrows
    @Step("Assert files are merged correctly")
    public static void assertMergedFiles(@NotNull final List<Path> filesToMerge, @NotNull final Path outputFile) {
        try (BufferedReader mainReader = Files.newBufferedReader(outputFile)) {
            for (Path file : filesToMerge) {
                assertMergedFile(mainReader, file);
            }
        }
        
    }
    
    @Step("Assert file lines are equal as lines of output file")
    private static void assertMergedFile(
            @NotNull final BufferedReader mainReader, @NotNull final Path file) throws IOException {
        
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
