package com.gsc.mkformularios.config;

import com.rg.dealer.Dealer;
import com.sc.commons.comunications.Mail;


public class ApplicationConfiguration {

    public static final int ID_APPLICATION_PVM_TOYOTA	= 33;
    public static final int ID_PRF_TOYOTA_TCAP			= 130;
    public static final int ID_PRF_TOYOTA_DEALER		= 131;
    public static final int ID_PRF_TOYOTA_CA			= 205;

    public static final int ID_APPLICATION_PVM_LEXUS	= 10010;
    public static final int ID_PRF_LEXUS_TCAP			= 10026;
    public static final int ID_PRF_LEXUS_DEALER			= 10027;


    public  int getPVMidApp(String oidNet) {
        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            return ID_APPLICATION_PVM_TOYOTA;
        else if (oidNet.equals(Dealer.OID_NET_LEXUS))
            return ID_APPLICATION_PVM_LEXUS;
        return -1;
    }

    public static String getMailFrom(String oidNet) {
        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            return Mail.MAIL_ADDRESS_EXTRANET_TOYOTA;
        else if (oidNet.equals(Dealer.OID_NET_LEXUS))
            return Mail.MAIL_ADDRESS_EXTRANET_LEXUS;

        return null;
    }
}
