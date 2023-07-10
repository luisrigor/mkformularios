package com.gsc.mkformularios.utils;

import com.gsc.mkformularios.dto.DealerDTO;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class DealersUtils {

    public List<DealerDTO> getMapDealers(String oidNet) throws SCErrorException {
        Hashtable<String, Dealer> mapDealers = null;
        List<DealerDTO> dealerDTOS = new ArrayList<>();

        if (oidNet.equals(Dealer.OID_NET_TOYOTA))
            mapDealers = Dealer.getToyotaHelper().getAllDealers();
        else
            mapDealers = Dealer.getLexusHelper().getAllDealers();

        mapDealers.put(Dealer.OID_NMSC, Dealer.getHelper().getByObjectId(oidNet, Dealer.OID_NMSC));

        Set<String> keySet = mapDealers.keySet();
        for (String key : keySet) {
            dealerDTOS.add(new DealerDTO(key, mapDealers.get(key)));
        }

        return dealerDTOS;
    }

    public Vector<String[]> getRetailerDealers(String oidNet) throws SCErrorException {

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

    public Dealer getDealerById(String oidNet, String oidDealer) throws SCErrorException {
        return Dealer.getHelper().getByObjectId(oidNet, oidDealer);
    }

    public Hashtable<String, Dealer>  getAllActiveMainDealers(String oidNet) throws SCErrorException {
        return Dealer.getToyotaHelper().getAllActiveMainDealers(oidNet);

    }

    public static Map<String, Dealer> convertHashDealers(Hashtable<String, Dealer> allDealersByOid) {

        Map<String, Dealer> mapDlrs = new Hashtable<String, Dealer>();
        Enumeration<Dealer> enumDlrs = allDealersByOid.elements();
        while (enumDlrs.hasMoreElements()) {
            Dealer oDealer = enumDlrs.nextElement();
            mapDlrs.put(oDealer.getDealerCode(), oDealer);
        }
        return mapDlrs;
    }
}



