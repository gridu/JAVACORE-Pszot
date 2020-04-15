package com.griddynamics.task_1.util;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public class SizeUnit {
    
    private UnitType unit;
    
    private double value;
    
    public SizeUnit(@NotNull final UnitType unit, final long value) {
        this.unit = unit;
        this.value = value;
    }
    
    public double getValueIn(@NotNull final UnitType unit) {
        if (this.unit == unit) return value;
        double valueModifier = (double) this.unit.bytesPerUnit / unit.bytesPerUnit;
        return value * valueModifier;
    }
    
    public SizeUnit convertTo(@NotNull final UnitType unit) {
        this.value = getValueIn(unit);
        this.unit = unit;
        return this;
    }
    
    public SizeUnit add(final double toAdd) {
        this.value += toAdd;
        return this;
    }
    
    public SizeUnit subtract(final double toSubtract) {
        this.value -= toSubtract;
        return this;
    }
    
    @Getter
    public enum UnitType {
        BYTE(1L),
        KB(1024L),
        MB(KB.bytesPerUnit * 1024L),
        GB(MB.bytesPerUnit * 1024L);
        
        private final long bytesPerUnit;
        
        UnitType(final long bytesPerUnit) {
            this.bytesPerUnit = bytesPerUnit;
        }
    }
    
}
