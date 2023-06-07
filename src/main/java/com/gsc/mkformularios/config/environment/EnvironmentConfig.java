package com.gsc.mkformularios.config.environment;

import com.gsc.a2p.invoke.A2pApiInvoke;
import com.gsc.microsoft.invoke.SharePointInvoke;
import com.gsc.scgscwsauthentication.invoke.SCAuthenticationInvoke;
import com.gsc.scwscardb.core.invoke.CarInvoker;

import java.util.Map;

public interface EnvironmentConfig {

    SCAuthenticationInvoke getAuthenticationInvoker();

    default SharePointInvoke getSharePointInvoker() {
        return new SharePointInvoke();
    }

    default String getMicrosoftApplicationId() {
        return com.gsc.microsoft.invoke.DATA.SHAREPOINT_SITE_RIGOR_DGAN_WEBAPPS;
    }

    CarInvoker getCarInvoker();

    A2pApiInvoke getA2pApiInvoker();

    Map<String, String> getEnvVariables();

}
