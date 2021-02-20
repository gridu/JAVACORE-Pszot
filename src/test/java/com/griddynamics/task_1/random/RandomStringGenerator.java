package com.griddynamics.task_1.random;

import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Pattern;

@Data
public class RandomStringGenerator {
    
    private final int minLength;
    
    private final int maxLength;
    
    public RandomStringGenerator(int minLength, int maxLength) {
        
        this.minLength = minLength;
        this.maxLength = maxLength;
    }
    
    public String generate() {
        
        Random random = new Random();
        byte[] array = new byte[random.nextInt(maxLength - minLength) + minLength];
        random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }
    
    public String generate(String regex) {
        
        Random random = new Random();
        Pattern pattern = Pattern.compile(regex);
        
        byte[] array = new byte[random.nextInt(maxLength - minLength) + minLength];
        for (int i = 0 ; i < array.length ; i++) {
            byte[] validChar = generateMatchingChar(random, pattern);
            array[i] = validChar[0];
        }
        
        return new String(array, StandardCharsets.UTF_8).trim();
    }
    
    private byte[] generateMatchingChar(Random random, Pattern pattern) {
        
        byte[] array;
        do {
            array = new byte[1];
            random.nextBytes(array);
        } while (!pattern.matcher(new String(array)).matches());
        return array;
    }
    
}
