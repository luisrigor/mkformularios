package com.gsc.mkformularios.config;

import com.gsc.as400.initialization.AS400ConnectionPooling;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class LoggingConfig {
    //TODO replace @SLf4j for LOGGER CLASS
    //TODO cambiar log4j2.properties por log4j.properties basado en archivo provisto
    @Value("${log4j.configuration.path}")
    private String log4jConfigPath;

    @Value("${app.server.type}")
    private int SERVER_TYPE;

    @Bean
    public void configureLogging() throws IOException {
        ClassPathResource log4jResource = new ClassPathResource(log4jConfigPath);
        PropertyConfigurator.configure(log4jResource.getInputStream());
//        AS400ConnectionPooling.setData(SERVER_TYPE);
    }
}
