package com.gsc.mkformularios.config.datasource.toyota;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.datasource.second")
@Getter
@Setter
public class DBTwoConfig {

    private String url;
    private String password;
    private String username;
    private String jndi;
}
