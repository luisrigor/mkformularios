package com.gsc.mkformularios.config.environment;

import com.gsc.ws.util.DATA;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MapProfileVariables {


    public static Map<String, String> getEnvVariablesLocal() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("CONSUMER_DOMAIN_TAAS_SERVER", com.gsc.consumer.util.DATA.TAAS_SERVER_STAGING);
        envVariables.put("CONSUMER_DOMAIN_USER_NAME", com.gsc.consumer.util.DATA.USER_NAME_STAGING);
        envVariables.put("CONSUMER_DOMAIN_PASSWORD",  com.gsc.consumer.util.DATA.USER_PASSWORD_STAGING);
        envVariables.put("CONSUMER_DOMAIN_SERVER", com.gsc.consumer.util.DATA.API_SERVER_STAGING);
        envVariables.put("CONSENT_CENTER_URL", com.gsc.consent.util.DATA.CONSENT_CENTER_URL_STAGING_HTTPS);
        envVariables.put("AS400_WEBSERVICE_ADDRESS", com.gsc.ws.util.DATA.SERVER_STAGING_HTTPS);
        envVariables.put("DATA_TOKEN", DATA.TOKEN_STAGING);

        return envVariables;
    }

    public static Map<String, String> getEnvVariablesDevelopment() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("CONSUMER_DOMAIN_TAAS_SERVER", com.gsc.consumer.util.DATA.TAAS_SERVER_STAGING);
        envVariables.put("CONSUMER_DOMAIN_USER_NAME", com.gsc.consumer.util.DATA.USER_NAME_STAGING);
        envVariables.put("CONSUMER_DOMAIN_PASSWORD",  com.gsc.consumer.util.DATA.USER_PASSWORD_STAGING);
        envVariables.put("CONSUMER_DOMAIN_SERVER", com.gsc.consumer.util.DATA.API_SERVER_STAGING);
        envVariables.put("CONSENT_CENTER_URL", com.gsc.consent.util.DATA.CONSENT_CENTER_URL_STAGING_HTTPS);
        envVariables.put("AS400_WEBSERVICE_ADDRESS", com.gsc.ws.util.DATA.SERVER_STAGING_HTTPS);
        envVariables.put("DATA_TOKEN", DATA.TOKEN_STAGING);

        return envVariables;
    }

    public static Map<String, String> getEnvVariablesStaging() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("CONSUMER_DOMAIN_TAAS_SERVER", com.gsc.consumer.util.DATA.TAAS_SERVER_STAGING);
        envVariables.put("CONSUMER_DOMAIN_USER_NAME", com.gsc.consumer.util.DATA.USER_NAME_STAGING);
        envVariables.put("CONSUMER_DOMAIN_PASSWORD",  com.gsc.consumer.util.DATA.USER_PASSWORD_STAGING);
        envVariables.put("CONSUMER_DOMAIN_SERVER", com.gsc.consumer.util.DATA.API_SERVER_STAGING);
        envVariables.put("CONSENT_CENTER_URL", com.gsc.consent.util.DATA.CONSENT_CENTER_URL_STAGING_HTTPS);
        envVariables.put("AS400_WEBSERVICE_ADDRESS", com.gsc.ws.util.DATA.SERVER_STAGING_HTTPS);
        envVariables.put("DATA_TOKEN", DATA.TOKEN_STAGING);

        return envVariables;
    }

    public static Map<String, String> getEnvVariablesProduction() {
        Map<String, String> envVariables = new HashMap<>();
        envVariables.put("CONSUMER_DOMAIN_TAAS_SERVER", com.gsc.consumer.util.DATA.TAAS_SERVER_PRODUCTION);
        envVariables.put("CONSUMER_DOMAIN_USER_NAME", com.gsc.consumer.util.DATA.TAAS_USER_NAME_PRODUCTION);
        envVariables.put("CONSUMER_DOMAIN_PASSWORD",  com.gsc.consumer.util.DATA.TAAS_USER_PASSWORD_PRODUCTION);
        envVariables.put("CONSUMER_DOMAIN_SERVER", com.gsc.consumer.util.DATA.API_SERVER_PRODUCTION);
        envVariables.put("CONSENT_CENTER_URL", com.gsc.consent.util.DATA.CONSENT_CENTER_URL_PRODUCTION_HTTPS);
        envVariables.put("AS400_WEBSERVICE_ADDRESS", DATA.SERVER_PRODUCTION_HTTPS);
        envVariables.put("DATA_TOKEN", DATA.TOKEN_PRODUCTION);

        return envVariables;
    }

}
