package com.griddynamics.task_1.sorting;

import com.griddynamics.task_1.FileSplitter;
import com.griddynamics.task_1.random.RandomFileGenerator;
import com.griddynamics.task_1.random.RandomStringGenerator;
import com.griddynamics.task_1.util.SizeUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.griddynamics.task_1.reporting.Assertions.assertSplitFiles;
import static com.griddynamics.task_1.util.SizeEstimator.getSizeUnitOfFile;
import static com.griddynamics.task_1.util.SizeUnit.UnitType.KB;

public class FileSplitterTest {
    
    private static final Logger LOGGER = Logger.getLogger(FileSplitterTest.class.getName());
    
    private final int MAX_TEMP_FILE_SIZE_KB = 32;
    
    private static RandomFileGenerator fileGenerator;
    
    private static RandomStringGenerator fileLineGenerator;
    
    private Path fileToSplit;
    
    private List<Path> splitFiles;
    
    @BeforeTest
    public static void setUp() {
    
        fileGenerator = new RandomFileGenerator(new SizeUnit(KB, 128));
        fileLineGenerator = new RandomStringGenerator(10, 30);
    }
    
    @BeforeMethod
    public void prepareTest() {
    
        createTempFile();
    }
    
    private void createTempFile() {
    
        fileToSplit = fileGenerator.createRandomFile(fileLineGenerator);
        LOGGER.log(Level.INFO, "Temporary file successfully created: " + fileToSplit.getFileName());
    }
    
    @AfterMethod
    public void deleteFiles() {
        
        deleteFile(fileToSplit);
        for (Path file : splitFiles) {
            deleteFile(file);
        }
        splitFiles.clear();
    }
    
    public void deleteFile(Path path) {
        
        File file = path.toFile();
        if (file.delete()) {
            LOGGER.log(Level.INFO, "Temporary file successfully deleted: " + file.getName());
        }
    }
    
    @Test
    public void shouldSplitFile() {
        
        SizeUnit maxTempFileSizeUnit = new SizeUnit(KB, MAX_TEMP_FILE_SIZE_KB);
        splitFiles = splitFile(fileToSplit, maxTempFileSizeUnit);
        assertSplitFiles(getSizeUnitOfFile(fileToSplit, KB), maxTempFileSizeUnit, splitFiles);
    }
    
    
    private List<Path> splitFile(Path fileToSplit, SizeUnit maxTempFileSizeUnit) {
        
        splitFiles = new FileSplitter(fileToSplit, maxTempFileSizeUnit).split();
        return splitFiles;
    }
    
}
