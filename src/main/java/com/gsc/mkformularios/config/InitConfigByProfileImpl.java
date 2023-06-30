package com.gsc.mkformularios.config;

import com.gsc.mkformularios.constants.ConsumerDataConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"local", "development"})
@Configuration
public class InitConfigByProfileImpl implements InitConfigByProfile {

    public static String CONSUMER_DOMAIN_TAAS_SERVER;
    public static String CONSUMER_DOMAIN_USER_NAME;
    public static String CONSUMER_DOMAIN_PASSWORD;
    public static String CONSUMER_DOMAIN_SERVER;

    public static String CONSENT_CENTER_URL;

    @Profile("local")
    @Override
    public void setEnvVariables() {
        CONSUMER_DOMAIN_TAAS_SERVER = ConsumerDataConstants.TAAS_SERVER_STAGING;
        CONSUMER_DOMAIN_USER_NAME = ConsumerDataConstants.USER_NAME_STAGING;
        CONSUMER_DOMAIN_PASSWORD = ConsumerDataConstants.USER_PASSWORD_STAGING;
        CONSUMER_DOMAIN_SERVER = ConsumerDataConstants.API_SERVER_STAGING;
        CONSENT_CENTER_URL = ConsumerDataConstants.CONSENT_CENTER_URL_PROD;
    }



}
