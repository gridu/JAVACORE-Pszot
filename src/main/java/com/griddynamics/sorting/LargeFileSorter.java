package com.griddynamics.sorting;

import com.griddynamics.FileMerger;
import com.griddynamics.FileSplitter;
import com.griddynamics.comparator.BasicFileComparator;
import com.griddynamics.config.Configuration;
import com.griddynamics.util.SizeUnit;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import static com.griddynamics.util.SizeEstimator.isLargeFile;

@Data
public class LargeFileSorter implements FileSorter {
    
    private static final Configuration config = Configuration.getInstance();
    
    private static final BasicFileComparator FILE_COMPARATOR = new BasicFileComparator();
    
    private Comparator<String> comparator;
    
    private SizeUnit maxTempFileSize;
    
    private BasicFileSorter tempFileSorter;
    
    public LargeFileSorter(@NotNull final Comparator<String> lineComparator,
                           @NotNull final SizeUnit maxTempFileSize) {
        
        this.comparator = lineComparator;
        this.maxTempFileSize = maxTempFileSize;
        tempFileSorter = new BasicFileSorter(lineComparator);
    }
    
    @Override
    public void sort(@NotNull final Path file) {
        if (!isLargeFile(file, config.getMaxFileSizeUntilSplit())) {
            sortTempFile(file);
            return;
        }
        
        List<Path> tempFiles = splitFiles(file);
        sortTempFiles(tempFiles);
        mergeFiles(tempFiles, file);
        deleteTempFiles(tempFiles);
    }
    
    private void sortTempFiles(@NotNull final List<Path> tempFiles) {
        Flux.fromIterable(tempFiles)
                .parallel()
                .doOnEach(tempFileSignal -> sortTempFile(tempFileSignal.get()))
                .subscribe()
                .dispose();
    }
    
    private void sortTempFile(final Path tempFile) {
        if (tempFile == null) return;
        tempFileSorter.sort(tempFile);
    }
    
    @NotNull
    private List<Path> splitFiles(final Path fileToSplit) {
        FileSplitter splitter = new FileSplitter(fileToSplit, maxTempFileSize);
        return splitter.split();
    }
    
    private void mergeFiles(@NotNull final List<Path> tempFiles, final Path outputFile) {
        FileMerger fileMerger = new FileMerger(tempFiles, outputFile, FILE_COMPARATOR);
        fileMerger.merge();
    }
    
    private void deleteTempFiles(@NotNull final List<Path> tempFiles) {
        for (Path tempFile : tempFiles) {
            tempFile.toFile().delete();
        }
    }
    
}
