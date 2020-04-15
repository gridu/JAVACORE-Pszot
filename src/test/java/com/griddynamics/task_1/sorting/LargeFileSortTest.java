package com.griddynamics.task_1.sorting;

import com.griddynamics.task_1.comparator.BasicStringComparator;
import com.griddynamics.task_1.config.Configuration;
import com.griddynamics.task_1.random.RandomFileGenerator;
import com.griddynamics.task_1.random.RandomStringGenerator;
import com.griddynamics.task_1.sorting.validation.SortedFileValidator;
import io.qameta.allure.Step;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.griddynamics.task_1.reporting.Assertions.assertSortedFile;

public class LargeFileSortTest {
    
    private static final Logger LOGGER = Logger.getLogger(LargeFileSortTest.class.getName());
    
    private static final Configuration config = Configuration.getInstance();
    
    private static Comparator<String> comparator;
    
    private static RandomFileGenerator fileGenerator;
    
    private static RandomStringGenerator fileLineGenerator;
    
    private Path largeFile;
    
    private SortedFileValidator fileValidator;
    
    @BeforeTest
    public static void setUp() {
        comparator = new BasicStringComparator();
        fileGenerator = new RandomFileGenerator(config.getMaxFileSizeUntilSplit().add(128));
        fileLineGenerator = new RandomStringGenerator(30, 60);
    }
    
    @BeforeMethod
    public void prepareTest() {
        createTempFile();
        createValidator();
    }
    
    @Step("Create temporary file")
    private void createTempFile() {
        largeFile = fileGenerator.createRandomFile(fileLineGenerator);
        LOGGER.log(Level.INFO, "Temporary file successfully created: " + largeFile.getFileName());
    }
    
    @Step("Create sort validator")
    private void createValidator() {
        fileValidator = new SortedFileValidator(largeFile, comparator);
    }
    
    @AfterMethod
    public void deleteTempFile() {
        File file = largeFile.toFile();
        if (file.delete()) {
            LOGGER.log(Level.INFO, "Temporary file successfully deleted: " + file.getName());
        }
    }
    
    @Test
    public void shouldSortLargeFile() {
        new LargeFileSorter(comparator, config.getMaxTempFileSize()).sort(largeFile);
        assertSortedFile(largeFile, fileValidator);
    }
    
}
