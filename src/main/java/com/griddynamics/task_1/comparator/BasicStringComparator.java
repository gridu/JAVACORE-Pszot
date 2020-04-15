package com.griddynamics.task_1.comparator;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class BasicStringComparator implements Comparator<String> {
    
    @Override
    public int compare(@NotNull final String left, @NotNull final String right) {
        return left.compareTo(right);
    }
    
}
