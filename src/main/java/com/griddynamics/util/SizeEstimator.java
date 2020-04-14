package com.griddynamics.util;

import com.griddynamics.util.SizeUnit.UnitType;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.griddynamics.util.SizeUnit.UnitType.BYTE;
import static com.griddynamics.util.SizeUnit.UnitType.KB;

public final class SizeEstimator {
    
    private SizeEstimator() {
    
    }
    
    @NotNull
    @SneakyThrows
    public static SizeUnit getSizeUnitOfFile(@NotNull final Path filePath, @NotNull final UnitType unitType) {
        long size = Files.size(filePath);
        return new SizeUnit(BYTE, size).convertTo(unitType);
    }
    
    @NotNull
    @SneakyThrows
    public static SizeUnit getSizeUnitOfString(@NotNull final String string, @NotNull final UnitType unitType) {
        int size = string.getBytes().length;
        return new SizeUnit(BYTE, size).convertTo(unitType);
    }
    
    @SneakyThrows
    public static double getSizeOfFile(@NotNull final Path filePath, @NotNull final UnitType unitType) {
        return getSizeUnitOfFile(filePath, unitType).getValue();
    }
    
    @SneakyThrows
    public static double getSizeOfString(@NotNull final String string, @NotNull final UnitType unitType) {
        return getSizeUnitOfString(string, unitType).getValue();
    }
    
    public static boolean isLargeFile(
            @NotNull final Path filePath, @NotNull final SizeUnit maxSizeSinceLargeFile) {
        return getSizeOfFile(filePath, KB) > maxSizeSinceLargeFile.getValueIn(KB);
    }
    
    public static void ensureAdmissibleFileSize(
            @NotNull final Path filePath, @NotNull final SizeUnit maxFileSize) throws IOException {
        
        if (isLargeFile(filePath, maxFileSize)) {
            throw new IOException(
                    "File is too large. Please use external sorting instead." +
                            " File size: <" + SizeEstimator.getSizeOfFile(filePath, KB) + "KB>");
        }
    }
    
}
