package com.griddynamics.task_1.util;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SortingUtils {
    
    private SortingUtils() {
    }
    
    @NotNull
    public static List<String> sortStringListInParallel(
            @NotNull final List<String> strings, @NotNull final Comparator<String> comparator) {
        
        return Flux.fromIterable(strings)
                .parallel()
                .collectSortedList(comparator)
                .blockOptional()
                .orElse(new ArrayList<>());
        
    }
    
}
