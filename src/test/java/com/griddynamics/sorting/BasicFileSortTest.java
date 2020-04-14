package com.griddynamics.sorting;

import com.griddynamics.comparator.BasicStringComparator;
import com.griddynamics.config.Configuration;
import com.griddynamics.random.RandomFileGenerator;
import com.griddynamics.random.RandomStringGenerator;
import com.griddynamics.sorting.validation.SortedFileValidator;
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

import static com.griddynamics.reporting.AllureAttachments.attachFileToSort;
import static com.griddynamics.reporting.AllureAttachments.attachSortedFile;
import static com.griddynamics.reporting.Assertions.assertSortedFile;

public class BasicFileSortTest {
    
    private static final Logger LOGGER = Logger.getLogger(BasicFileSortTest.class.getName());
    
    private static final Configuration config = Configuration.getInstance();
    
    private static Comparator<String> comparator;
    
    private static RandomFileGenerator fileGenerator;
    
    private static RandomStringGenerator fileLineGenerator;
    
    private Path fileToSort;
    
    private SortedFileValidator fileValidator;
    
    @BeforeTest
    public static void setUp() {
        comparator = new BasicStringComparator();
        fileGenerator = new RandomFileGenerator(config.getMaxFileSizeUntilSplit());
        fileLineGenerator = new RandomStringGenerator(7, 8);
    }
    
    @BeforeMethod
    public void prepareTest() {
        createTempFile();
        createValidator();
    }
    
    @Step("Create temporary file")
    private void createTempFile() {
        fileToSort = fileGenerator.createRandomFile(fileLineGenerator);
        LOGGER.log(Level.INFO, "Temporary file successfully created: " + fileToSort.getFileName());
    }
    
    @Step("Create sort validator")
    private void createValidator() {
        fileValidator = new SortedFileValidator(fileToSort, comparator);
    }
    
    @AfterMethod
    public void deleteTempFile() {
        File file = fileToSort.toFile();
        if (file.delete()) {
            LOGGER.log(Level.INFO, "Temporary file successfully deleted: " + file.getName());
        }
    }
    
    @Test
    public void shouldSortSmallFile() {
        attachFileToSort(fileToSort);
        new BasicFileSorter(comparator).sort(fileToSort);
        attachSortedFile(fileToSort);
        assertSortedFile(fileToSort, fileValidator);
    }
    
}
