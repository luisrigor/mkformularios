package com.gsc.mkformularios.config;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;

@Configuration
public class LoggingConfig {
    @Value("${log4j.configuration.path}")
    private String log4jConfigPath;

    @Value("${app.server.type}")
    private int SERVER_TYPE;

    @Bean
    public void configureLogging() throws IOException {
        ClassPathResource log4jResource = new ClassPathResource(log4jConfigPath);
        PropertyConfigurator.configure(log4jResource.getInputStream());
    }
}
