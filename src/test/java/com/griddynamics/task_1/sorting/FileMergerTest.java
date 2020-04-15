package com.griddynamics.task_1.sorting;

import com.griddynamics.task_1.FileMerger;
import com.griddynamics.task_1.comparator.BasicFileComparator;
import com.griddynamics.task_1.random.RandomFileGenerator;
import com.griddynamics.task_1.random.RandomStringGenerator;
import com.griddynamics.task_1.util.FileUtils;
import com.griddynamics.task_1.util.SizeUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.griddynamics.task_1.reporting.Assertions.assertMergedFiles;
import static com.griddynamics.task_1.util.SizeUnit.UnitType.KB;

public class FileMergerTest {
    
    private static final int FILES_TO_MERGE_AMOUNT = 5;
    
    private static final int FILE_TO_MERGE_SIZE_KB = 10;
    
    private static final Comparator<Path> comparator = new BasicFileComparator();
    
    private static RandomFileGenerator fileGenerator;
    
    private static RandomStringGenerator fileLineGenerator;
    
    private List<Path> filesToMerge = new ArrayList<>();
    
    private Path outputFile;
    
    @BeforeTest
    public static void prepareTest() {
        fileGenerator = new RandomFileGenerator(new SizeUnit(KB, FILE_TO_MERGE_SIZE_KB));
        fileLineGenerator = new RandomStringGenerator(7, 8);
    }
    
    @BeforeMethod
    public void setUp() throws IOException {
        outputFile = FileUtils.createTempFile("outputFile");
        for (int i = 0; i < FILES_TO_MERGE_AMOUNT; i++) {
            Path file = fileGenerator.createRandomFile(fileLineGenerator);
            filesToMerge.add(file);
        }
    }
    
    @AfterMethod
    public void turnDown() {
        outputFile.toFile().delete();
        Flux.fromIterable(filesToMerge).map(Path::toFile).toIterable().forEach(File::delete);
    }
    
    @Test
    public void shouldMergeFiles() {
        FileMerger merger = new FileMerger(filesToMerge, outputFile, comparator);
        merger.merge();
        assertMergedFiles(filesToMerge, outputFile);
    }
    
}
