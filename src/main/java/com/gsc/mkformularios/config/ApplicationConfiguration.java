package com.gsc.mkformularios.config;

import com.rg.dealer.Dealer;


public class ApplicationConfiguration {

    public static final int ID_APPLICATION_PVM_TOYOTA	= 33;
    public static final int ID_PRF_TOYOTA_TCAP			= 130;
    public static final int ID_PRF_TOYOTA_DEALER		= 131;
    public static final int ID_PRF_TOYOTA_CA			= 205;

    public static final int ID_APPLICATION_PVM_LEXUS	= 10010;
    public static final int ID_PRF_LEXUS_TCAP			= 10026;
    public static final int ID_PRF_LEXUS_DEALER			= 10027;

    public static final String MAIL_ADDRESS_EXTRANET_TOYOTA = "extranettoyota@toyotacaetano.pt";
    public static final String MAIL_ADDRESS_EXTRANET_LEXUS = "extranet@lexus.pt";
    public static final String DATASOURCE_USRLOGON		= "jdbc/usrlogon";

    public static final String TVC_MANAGER_ROLE_ACTIVE_DEALERS			= "ACTIVE_DEALERS";



    public  int getPVMidApp(String oidNet) {
        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            return ID_APPLICATION_PVM_TOYOTA;
        else if (oidNet.equals(Dealer.OID_NET_LEXUS))
            return ID_APPLICATION_PVM_LEXUS;
        return -1;
    }

    public static String getMailFrom(String oidNet) {
        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            return MAIL_ADDRESS_EXTRANET_TOYOTA;
        else if (oidNet.equals(Dealer.OID_NET_LEXUS))
            return MAIL_ADDRESS_EXTRANET_LEXUS;

        return null;
    }

    public static String getUsrlogonViewName(String oidNet) {
        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            return "TOYOTA_USER_ENTITY_PROFILE";
        else if (oidNet.equals(Dealer.OID_NET_LEXUS))
            return "LEXUS_USER_ENTITY_PROFILE";

        return null;
    }

    public static String getViewDealers(String oidNet) {
        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            return "TOYOTA_RETAILER";
        else if (oidNet.equals(Dealer.OID_NET_LEXUS))
            return "LEXUS_RETAILER";

        return null;
    }
}
