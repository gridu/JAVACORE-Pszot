package com.griddynamics.config;

import com.griddynamics.util.SizeUnit;
import lombok.Data;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Properties;

import static com.griddynamics.util.SizeUnit.UnitType.KB;
import static java.lang.Integer.parseInt;

@Data
public final class Configuration {
    
    @NotNull
    public final Properties properties = new Properties();
    
    private final SizeUnit maxFileSizeUntilSplit;
    
    private final SizeUnit maxTempFileSize;
    
    private Configuration() {
        loadProperties();
        this.maxFileSizeUntilSplit = new SizeUnit(KB, parseInt(properties.getProperty("max-file-size-until-split")));
        this.maxTempFileSize = new SizeUnit(KB, parseInt(properties.getProperty("max-temp-file-size")));
    }
    
    @SneakyThrows
    private void loadProperties() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream propertiesStream = loader.getResourceAsStream("application.properties");
        if (propertiesStream == null) return;
        
        properties.load(propertiesStream);
    }
    
    public static Configuration getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    private static class SingletonHelper {
        
        private static final Configuration INSTANCE = new Configuration();
        
    }
    
}
