package com.app.mowerproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${file-path}")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }
}
