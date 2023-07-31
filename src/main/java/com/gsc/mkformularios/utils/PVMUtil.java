package com.gsc.mkformularios.utils;

import com.rg.dealer.Dealer;
import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DataBaseTasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import static com.gsc.mkformularios.service.impl.pvm.PVM1MonthReport.CalculatePVMDtFrom;

public class PVMUtil {

    public final static String DLR_CODE_FROTAS_SUL	 	= "75";
    public final static String DLR_CODE_IMOBILIZADO 	= "99";
    public final static String DLR_CODE_FROTAS_GAIA	 	= "40";
    public final static String DLR_CODE_EXPORTACAO		= "32";

    public final static int SUB_DLR_CODE_FROTAS_SUL		= 1;
    public final static int SUB_DLR_CODE_IMOBILIZADO	= 2;
    public final static int SUB_DLR_CODE_FROTAS_GAIA	= 3;
    public final static int SUB_DLR_CODE_EXPORTACAO		= 4;

    public static Hashtable<String, Dealer> convertHashDealers(Hashtable<String, Dealer> allDealersByOid) {

        Hashtable<String, Dealer> mapDlrs = new Hashtable<String, Dealer>();
        Enumeration<Dealer> enumDlrs = allDealersByOid.elements();
        while (enumDlrs.hasMoreElements()) {
            Dealer oDealer = enumDlrs.nextElement();
            mapDlrs.put(oDealer.getDealerCode(), oDealer);
        }
        return mapDlrs;
    }

    public static Vector<String[]> getRetailerDealers(String oidNet) throws SCErrorException {

        Vector<String[]> vecResult = new Vector<String[]>();
        Vector<Dealer> vecDealers = null;
        if (oidNet.equalsIgnoreCase(Dealer.OID_NET_TOYOTA))
            vecDealers = Dealer.getToyotaHelper().getAllActiveRetailer();
        else if (oidNet.equalsIgnoreCase(Dealer.OID_NET_LEXUS))
            vecDealers = Dealer.getLexusHelper().getAllActiveRetailer();

        for (Dealer oDealer : vecDealers) {
            vecResult.addElement(new String[]{oDealer.getObjectId(), oDealer.getDealerCode() + "-" + oDealer.getDesig(), oDealer.getDealerCode(), "0"});//Zero significa subdealer
        }
        return vecResult;

    }



}
