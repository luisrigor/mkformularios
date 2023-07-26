package com.gsc.mkformularios.utils;

import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.rg.dealer.Dealer;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class PVMReportQueries3Month {

    public static String getPlatesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType, boolean isSalesFrotas) {

        StringBuilder retValue = new StringBuilder();

        retValue.append(makeSubQuery1(isSalesFrotas));
        retValue.append(  "(VALUES\n" );

        retValue.append(makeSubQuery2(isSalesFrotas, vecRetailerDealers));

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cod Concessionário\",\nDEALER AS \"Concessionário\", \n");


        StringBuilder cTemp = new StringBuilder();

        for (String[] model :vecPVMCarModel) {
            cTemp.append(makeQueryFromModel(model));
        }

        cTemp.append("CASE WHEN SUM(M0) IS NULL THEN SUM(ORC0) ELSE SUM(M0) END AS \"TT-M1\", \n");
        cTemp.append("SUM(ORC0) AS \"ORC-M1\", \n");
        cTemp.append("CASE WHEN SUM(M1) IS NULL THEN SUM(ORC1) ELSE SUM(M1) END AS \"TT-M2\", \n");
        cTemp.append("SUM(ORC1) AS \"ORC-M2\", \n");
        cTemp.append("CASE WHEN SUM(M2) IS NULL THEN SUM(ORC2) ELSE SUM(M2) END AS \"TT-M3\", \n");
        cTemp.append("SUM(ORC2) AS \"ORC-M3\", \n");
        cTemp.append("SUM(M0_ISORC) AS DUMMY \n");

        cTemp.append(  "FROM ( \n");
        cTemp.append(  "   SELECT \n");
        cTemp.append(  "   D.OID_DEALER, \n");
        cTemp.append(makeSubQuery3(isSalesFrotas));

        cTemp.append(  "   D.DEALERCODE, \n");
        cTemp.append(  "   D.DEALER, \n");
        cTemp.append(  "   M.ID AS ID_PVM_CARMODEL, \n");
        cTemp.append(  "   M.TYPE, \n");
        cTemp.append(  "   PMRD0.PLATES_VALUE AS M0, \n");
        cTemp.append(  "   PB0.PLATES AS ORC0, \n");
        cTemp.append(  "   CASE WHEN PMRD0.PLATES_VALUE IS NULL THEN 1 ELSE 0 END AS M0_ISORC, \n");
        cTemp.append(  "   PMRD0.PLATES_VALUE - PMRD1.PLATESD1  AS D1, \n");
        cTemp.append(  "   PMRD0.PLATES_VALUE2 AS M1, \n");
        cTemp.append(  "   CASE WHEN PMRD0.PLATES_VALUE2 IS NULL THEN 1 ELSE 0 END AS M1_ISORC, \n");
        cTemp.append(  "   PB1.PLATES AS ORC1, \n");
        cTemp.append(  "   PMRD0.PLATES_VALUE2 - PMRD1.PLATESD2 AS D2, \n");
        cTemp.append(  "   PMRD0.PLATES_VALUE3 AS M2, \n");
        cTemp.append(  "   PB2.PLATES AS ORC2, \n");
        cTemp.append(  "   CASE WHEN PMRD0.PLATES_VALUE3 IS NULL THEN 1 ELSE 0 END AS M2_ISORC \n");
        cTemp.append(  "   FROM DEALERS D \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   (SELECT * FROM PVM_CARMODEL WHERE ACTIVE = 'S' AND TYPE = '" + modelType + "' AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM AND VALUE(DT_TO, DATE('2099-12-31'))) M ON 1=1 \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(  "      PMR.OID_DEALER, \n");
        cTemp.append(makeSubQuery4(isSalesFrotas));

        cTemp.append(  "      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append(  "      PMRD.PLATES_VALUE, \n");
        cTemp.append(  "      PMRD.PLATES_VALUE2, \n");
        cTemp.append(  "      PMRD.PLATES_VALUE3, \n");
        cTemp.append(  "      0 AS ORC \n");
        cTemp.append(  "      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append(  "      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        cTemp.append(  "      WHERE PMR.YEAR = " + year + " \n");
        cTemp.append(  "      AND PMR.MONTH = " + month + " \n");
        cTemp.append(makeSubQuery5(isSalesFrotas));

        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(  "      PMR.OID_DEALER, \n");
        cTemp.append(makeSubQuery4(isSalesFrotas));

        cTemp.append(  "      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append(  "      PMRD.PLATES_VALUE2 AS PLATESD1, \n");
        cTemp.append(  "      PMRD.PLATES_VALUE3 AS PLATESD2 \n");
        cTemp.append(  "      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append(  "      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append(  "      WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append(  "      AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append(makeSubQuery6(isSalesFrotas));

        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(makeSubQuery7(isSalesFrotas));
        cTemp.append(  "      FROM PVM_BUDGET PB \n");

        cTemp.append(  "      WHERE PB.YEAR = " + year + " \n");
        cTemp.append(  "      AND PB.MONTH = " + month + " \n");
        cTemp.append(makeSubQuery8(isSalesFrotas));
        cTemp.append(  "LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(makeSubQuery9(isSalesFrotas));
        cTemp.append(  "      FROM PVM_BUDGET PB \n");

        //Adicionar 1 m�ses � data
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, 1);

        yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);
        cTemp.append(  "      WHERE PB.YEAR = " + yearTMP + " \n");
        cTemp.append(  "      AND PB.MONTH = " + monthTMP + " \n");
        cTemp.append(makeSubQuery10(isSalesFrotas));

        cTemp.append(  "LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(makeSubQuery11(isSalesFrotas));

        cTemp.append(  "      FROM PVM_BUDGET PB \n");

        //Adicionar 2 m�ses � data
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, 2);

        yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append(  "      WHERE PB.YEAR = " + yearTMP + " \n");
        cTemp.append(  "      AND PB.MONTH = " + monthTMP + " \n");
        cTemp.append(makeSubQuery12(isSalesFrotas));

        cTemp.append(  ") GRID \n");
        cTemp.append(  "GROUP BY DEALERCODE, DEALER \n");
        cTemp.append(  "ORDER BY DEALER \n");

        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getSalesSql( List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType, boolean isSalesFrotas) {

        StringBuilder retValue = new StringBuilder();

        retValue.append(makeSubQuery1(isSalesFrotas));
        retValue.append(  "(VALUES\n" );

        retValue.append(makeSubQuery2(isSalesFrotas, vecRetailerDealers));


        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");


        StringBuilder cTemp = new StringBuilder();

        for (String[] model :vecPVMCarModel) {
            cTemp.append(makeQueryFromModel(model));
        }

        cTemp.append("'' AS DUMMY \n");

        cTemp.append(  "FROM ( \n");
        cTemp.append(  "   SELECT \n");
        cTemp.append(  "   D.OID_DEALER, \n");
        cTemp.append(makeSubQuery3(isSalesFrotas));

        cTemp.append(  "   D.DEALERCODE, \n");
        cTemp.append(  "   D.DEALER, \n");
        cTemp.append(  "   M.ID AS ID_PVM_CARMODEL, \n");
        cTemp.append(  "   M.TYPE, \n");
        cTemp.append(  "   PMRD0.SALES_VALUE AS M0, \n");
        cTemp.append(  "   PMRD0.SALES_VALUE - PMRD1.SALESD1  AS D1, \n");
        cTemp.append(  "   PMRD0.SALES_VALUE2 AS M1, \n");
        cTemp.append(  "   PMRD0.SALES_VALUE2 - PMRD1.SALESD2 AS D2, \n");
        cTemp.append(  "   PMRD0.SALES_VALUE3 AS M2 \n");
        cTemp.append(  "   FROM DEALERS D \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   (SELECT * FROM PVM_CARMODEL WHERE ACTIVE = 'S' AND TYPE = '" + modelType + "' AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM AND VALUE(DT_TO, DATE('2099-12-31'))) M ON 1=1 \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(  "      PMR.OID_DEALER, \n");
        cTemp.append(  "      PMR.SUB_DEALER, \n");
        cTemp.append(  "      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append(  "      PMRD.SALES_VALUE, \n");
        cTemp.append(  "      PMRD.SALES_VALUE2, \n");
        cTemp.append(  "      PMRD.SALES_VALUE3 \n");
        cTemp.append(  "      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append(  "      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        cTemp.append(  "      WHERE PMR.YEAR = " + year + " \n");
        cTemp.append(  "      AND PMR.MONTH = " + month + " \n");
        cTemp.append(makeSubQuery5(isSalesFrotas));

        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(  "      PMR.OID_DEALER, \n");
        cTemp.append(makeSubQuery4(isSalesFrotas));

        cTemp.append(  "      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append(  "      PMRD.SALES_VALUE2 AS SALESD1, \n");
        cTemp.append(  "      PMRD.SALES_VALUE3 AS SALESD2 \n");
        cTemp.append(  "      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append(  "      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append(  "      WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append(  "      AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append(makeSubQuery6(isSalesFrotas));
        cTemp.append(  ") GRID \n");
        cTemp.append(  "GROUP BY DEALERCODE, DEALER \n");
        cTemp.append(  "ORDER BY DEALER \n");

        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getTotalPlatesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType) {

        StringBuilder retValue = new StringBuilder();

        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO retailDealerAct :vecRetailerDealers) {
            retValue.append(  "('" + retailDealerAct.getObjectid() + "', '" + retailDealerAct.getDealerCode() + "', '" + retailDealerAct.getDesig() + "'), \n"  );
        }

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");

        StringBuilder cTemp = new StringBuilder();

        cTemp.append("SUM(M0) AS  M0, \n");
        cTemp.append("SUM(M0) - SUM(PLATESD1) AS D1, \n");
        cTemp.append("SUM(ORC0) AS ORC0, \n");
        cTemp.append("SUM(M1) AS  M1, \n");
        cTemp.append("SUM(M1) - SUM(PLATESD2) AS D2, \n");
        cTemp.append("SUM(ORC1) AS ORC1, \n");
        cTemp.append("SUM(M2) AS  M2, \n");
        cTemp.append("SUM(ORC2) AS ORC2 \n");
        cTemp.append("FROM (  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("D.OID_DEALER,  \n");
        cTemp.append("D.DEALERCODE,  \n");
        cTemp.append("D.DEALER,  \n");
        cTemp.append("M.ID AS ID_PVM_CARMODEL,  \n");
        cTemp.append("M.TYPE,  \n");
        cTemp.append("PMRD0.PLATES_VALUE AS M0,  \n");
        cTemp.append("PB0.PLATES AS ORC0,  \n");
        cTemp.append("CASE WHEN PMRD0.PLATES_VALUE IS NULL THEN 1 ELSE 0 END AS M0_ISORC,  \n");
        cTemp.append("PMRD0.PLATES_VALUE - PMRD1.PLATESD1  AS D1,  \n");
        cTemp.append("PMRD0.PLATES_VALUE2 AS M1,  \n");
        cTemp.append("CASE WHEN PMRD0.PLATES_VALUE2 IS NULL THEN 1 ELSE 0 END AS M1_ISORC,  \n");
        cTemp.append("PB1.PLATES AS ORC1,  \n");
        cTemp.append("PMRD0.PLATES_VALUE2 - PMRD1.PLATESD2 AS D2,  \n");

        cTemp.append("PMRD0.PLATES_VALUE3 AS M2,  \n");
        cTemp.append("PB2.PLATES AS ORC2,  \n");
        cTemp.append("CASE WHEN PMRD0.PLATES_VALUE3 IS NULL THEN 1 ELSE 0 END AS M2_ISORC, \n");
        cTemp.append("PMRD1.PLATESD1, \n");
        cTemp.append("PMRD1.PLATESD2 \n");
        cTemp.append("FROM DEALERS D  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(SELECT * FROM PVM_CARMODEL WHERE ACTIVE = 'S' AND TYPE = '" + modelType + "' AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM AND VALUE(DT_TO, DATE('2099-12-31'))) M ON 1=1  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PMR.OID_DEALER,  \n");
        cTemp.append("PMRD.ID_PVM_CARMODEL,  \n");
        cTemp.append("PMRD.PLATES_VALUE,  \n");
        cTemp.append("PMRD.PLATES_VALUE2,  \n");
        cTemp.append("PMRD.PLATES_VALUE3,  \n");
        cTemp.append("0 AS ORC  \n");
        cTemp.append("FROM PVM_MONTHLYREPORTDETAIL PMRD  \n");
        cTemp.append("INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID  \n");



        cTemp.append("WHERE PMR.YEAR = " + year +"  \n");
        cTemp.append("AND PMR.MONTH = " + month + "  \n");
        cTemp.append(") PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER AND PMRD0.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PMR.OID_DEALER,  \n");
        cTemp.append("PMRD.ID_PVM_CARMODEL,  \n");
        cTemp.append("PMRD.PLATES_VALUE2 AS PLATESD1,  \n");
        cTemp.append("PMRD.PLATES_VALUE3 AS PLATESD2  \n");
        cTemp.append("FROM PVM_MONTHLYREPORTDETAIL PMRD  \n");
        cTemp.append("INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID  \n");

        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append("WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append("AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append(") PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER  AND PMRD1.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PB.OID_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC  \n");
        cTemp.append("FROM PVM_BUDGET PB  \n");

        cTemp.append("WHERE PB.YEAR = " + year + " \n");
        cTemp.append("AND PB.MONTH = " + month + " \n");
        cTemp.append(") PB0 ON D.OID_DEALER = PB0.OID_DEALER AND PB0.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PB.OID_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC  \n");
        cTemp.append("FROM PVM_BUDGET PB  \n");

        //Adicionar 1 m�s � data
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, +1);

        yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);


        cTemp.append("WHERE PB.YEAR = " + yearTMP + " \n");
        cTemp.append("AND PB.MONTH = " + monthTMP + " \n");
        cTemp.append(") PB1 ON D.OID_DEALER = PB1.OID_DEALER AND PB1.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PB.OID_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC  \n");
        cTemp.append("FROM PVM_BUDGET PB  \n");

        //Adicionar 2 meses � data
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, +2);

        yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);
        cTemp.append("WHERE PB.YEAR = " + yearTMP + " \n");
        cTemp.append("AND PB.MONTH = " + monthTMP + " \n");
        cTemp.append(") PB2 ON D.OID_DEALER = PB2.OID_DEALER AND PB2.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append(") GRID  \n");
        cTemp.append("GROUP BY DEALERCODE, DEALER  \n");
        cTemp.append("ORDER BY DEALER  \n");


        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getTotalPlatesFrotasSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType) {

        StringBuilder retValue = new StringBuilder();

        retValue.append(  "WITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );
        retValue.append(  "('"+ Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");

        StringBuilder cTemp = new StringBuilder();

        cTemp.append("SUM(M0) AS  M0, \n");
        cTemp.append("SUM(M0) - SUM(PLATESD1) AS D1, \n");
        cTemp.append("SUM(ORC0) AS ORC0, \n");
        cTemp.append("SUM(M1) AS  M1, \n");
        cTemp.append("SUM(M1) - SUM(PLATESD2) AS D2, \n");
        cTemp.append("SUM(ORC1) AS ORC1, \n");
        cTemp.append("SUM(M2) AS  M2, \n");
        cTemp.append("SUM(ORC2) AS ORC2 \n");
        cTemp.append("FROM (  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("D.OID_DEALER,  \n");
        cTemp.append("D.SUB_DEALER,  \n");
        cTemp.append("D.DEALERCODE,  \n");
        cTemp.append("D.DEALER,  \n");
        cTemp.append("M.ID AS ID_PVM_CARMODEL,  \n");
        cTemp.append("M.TYPE,  \n");
        cTemp.append("PMRD0.PLATES_VALUE AS M0,  \n");
        cTemp.append("PB0.PLATES AS ORC0,  \n");
        cTemp.append("CASE WHEN PMRD0.PLATES_VALUE IS NULL THEN 1 ELSE 0 END AS M0_ISORC,  \n");
        cTemp.append("PMRD0.PLATES_VALUE - PMRD1.PLATESD1  AS D1,  \n");
        cTemp.append("PMRD0.PLATES_VALUE2 AS M1,  \n");
        cTemp.append("CASE WHEN PMRD0.PLATES_VALUE2 IS NULL THEN 1 ELSE 0 END AS M1_ISORC,  \n");
        cTemp.append("PB1.PLATES AS ORC1,  \n");
        cTemp.append("PMRD0.PLATES_VALUE2 - PMRD1.PLATESD2 AS D2,  \n");
        cTemp.append("PMRD0.PLATES_VALUE3 AS M2,  \n");
        cTemp.append("PB2.PLATES AS ORC2,  \n");
        cTemp.append("CASE WHEN PMRD0.PLATES_VALUE3 IS NULL THEN 1 ELSE 0 END AS M2_ISORC, \n");
        cTemp.append("PMRD1.PLATESD1, \n");
        cTemp.append("PMRD1.PLATESD2 \n");
        cTemp.append("FROM DEALERS D  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(SELECT * FROM PVM_CARMODEL WHERE ACTIVE = 'S' AND TYPE = '" + modelType + "' AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM AND VALUE(DT_TO, DATE('2099-12-31'))) M ON 1=1  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PMR.OID_DEALER,  \n");
        cTemp.append("PMR.SUB_DEALER,  \n");
        cTemp.append("PMRD.ID_PVM_CARMODEL,  \n");
        cTemp.append("PMRD.PLATES_VALUE,  \n");
        cTemp.append("PMRD.PLATES_VALUE2,  \n");
        cTemp.append("PMRD.PLATES_VALUE3,  \n");
        cTemp.append("0 AS ORC  \n");
        cTemp.append("FROM PVM_MONTHLYREPORTDETAIL PMRD  \n");
        cTemp.append("INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID  \n");
        cTemp.append("WHERE PMR.YEAR = " + year +"  \n");
        cTemp.append("AND PMR.MONTH = " + month + "  \n");
        cTemp.append(") PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER AND D.SUB_DEALER = PMRD0.SUB_DEALER AND PMRD0.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PMR.OID_DEALER,  \n");
        cTemp.append("PMR.SUB_DEALER,  \n");
        cTemp.append("PMRD.ID_PVM_CARMODEL,  \n");
        cTemp.append("PMRD.PLATES_VALUE2 AS PLATESD1,  \n");
        cTemp.append("PMRD.PLATES_VALUE3 AS PLATESD2  \n");
        cTemp.append("FROM PVM_MONTHLYREPORTDETAIL PMRD  \n");
        cTemp.append("INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID  \n");

        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append("WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append("AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append(") PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER AND D.SUB_DEALER = PMRD1.SUB_DEALER AND PMRD1.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PB.OID_DEALER, PB.SUB_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC  \n");
        cTemp.append("FROM PVM_BUDGET PB  \n");
        cTemp.append("WHERE PB.YEAR = " + year + " \n");
        cTemp.append("AND PB.MONTH = " + month + " \n");
        cTemp.append(") PB0 ON D.OID_DEALER = PB0.OID_DEALER AND D.SUB_DEALER = PB0.SUB_DEALER AND PB0.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PB.OID_DEALER, PB.SUB_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC  \n");
        cTemp.append("FROM PVM_BUDGET PB  \n");

        //Adicionar 1 m�s � data
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, +1);

        yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);


        cTemp.append("WHERE PB.YEAR = " + yearTMP + " \n");
        cTemp.append("AND PB.MONTH = " + monthTMP + " \n");
        cTemp.append(") PB1 ON D.OID_DEALER = PB1.OID_DEALER AND D.SUB_DEALER = PB1.SUB_DEALER AND PB1.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append("LEFT JOIN  \n");
        cTemp.append("(  \n");
        cTemp.append("SELECT  \n");
        cTemp.append("PB.OID_DEALER, PB.SUB_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC  \n");
        cTemp.append("FROM PVM_BUDGET PB  \n");

        //Adicionar 2 meses � data
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, +2);

        yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);
        cTemp.append("WHERE PB.YEAR = " + yearTMP + " \n");
        cTemp.append("AND PB.MONTH = " + monthTMP + " \n");
        cTemp.append(") PB2 ON D.OID_DEALER = PB2.OID_DEALER AND D.SUB_DEALER = PB2.SUB_DEALER AND PB2.ID_PVM_CARMODEL = M.ID  \n");
        cTemp.append(") GRID  \n");
        cTemp.append("GROUP BY DEALERCODE, DEALER  \n");
        cTemp.append("ORDER BY DEALER  \n");


        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getTotalSalesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType) {

        StringBuilder retValue = new StringBuilder();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO retailDealerAct :vecRetailerDealers) {
            retValue.append(  "('" + retailDealerAct.getObjectid() + "', '" + retailDealerAct.getDealerCode() + "', '" + retailDealerAct.getDesig() + "'), \n"  );
        }

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");

        StringBuilder cTemp = new StringBuilder();

        cTemp.append("SUM(M0) AS M0, \n");
        cTemp.append("SUM(D1) AS D1, \n");
        cTemp.append("SUM(PLT0) AS PLT0, \n");
        cTemp.append("SUM(M1) AS M1, \n");
        cTemp.append("SUM(D2) AS D2, \n");
        cTemp.append("SUM(PLT1) AS PLT1, \n");
        cTemp.append("SUM(M2) AS M2, \n");
        cTemp.append("SUM(PLT2) AS PLT2 \n");
        cTemp.append("FROM \n");
        cTemp.append("( \n");
        cTemp.append("   SELECT \n");
        cTemp.append("   D.OID_DEALER, \n");
        cTemp.append("   D.DEALERCODE, \n");
        cTemp.append("   D.DEALER, \n");
        cTemp.append("   M.ID AS ID_PVM_CARMODEL, \n");
        cTemp.append("   M.TYPE, \n");
        cTemp.append("   PMRD0.SALES_VALUE AS M0, \n");
        cTemp.append("   PMRD0.PLATES_VALUE AS PLT0, \n");
        cTemp.append("   PMRD0.SALES_VALUE - PMRD1.SALESD1 AS D1, \n");
        cTemp.append("   PMRD0.SALES_VALUE2 AS M1, \n");
        cTemp.append("   PMRD0.PLATES_VALUE2 AS PLT1, \n");
        cTemp.append("   PMRD0.SALES_VALUE2 - PMRD1.SALESD2 AS D2, \n");
        cTemp.append("   PMRD0.SALES_VALUE3 AS M2, \n");
        cTemp.append("   PMRD0.PLATES_VALUE3 AS PLT2 \n");
        cTemp.append("   FROM DEALERS D \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      * \n");
        cTemp.append("      FROM PVM_CARMODEL \n");
        cTemp.append("      WHERE ACTIVE = 'S' \n");
        cTemp.append("      AND TYPE = '" + modelType + "' \n");
        cTemp.append("      AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM \n");
        cTemp.append("      AND VALUE(DT_TO, DATE('2099-12-31')) \n");
        cTemp.append("   ) \n");
        cTemp.append("   M ON 1=1 \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      PMR.OID_DEALER, \n");
        cTemp.append("      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append("      PMRD.SALES_VALUE, \n");
        cTemp.append("      PMRD.SALES_VALUE2, \n");
        cTemp.append("      PMRD.SALES_VALUE3, \n");
        cTemp.append("      PMRD.PLATES_VALUE, \n");
        cTemp.append("      PMRD.PLATES_VALUE2, \n");
        cTemp.append("      PMRD.PLATES_VALUE3 \n");
        cTemp.append("      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append("      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        cTemp.append("      WHERE PMR.YEAR = " + year + " \n");
        cTemp.append("      AND PMR.MONTH = " + month + " \n");
        cTemp.append("   ) \n");
        cTemp.append("   PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER \n");
        cTemp.append("   AND PMRD0.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      PMR.OID_DEALER, \n");
        cTemp.append("      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append("      PMRD.SALES_VALUE2 AS SALESD1, \n");
        cTemp.append("      PMRD.SALES_VALUE3 AS SALESD2 \n");
        cTemp.append("      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append("      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");

        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append("      WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append("      AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append("   ) \n");
        cTemp.append("   PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER \n");
        cTemp.append("   AND PMRD1.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append(") \n");
        cTemp.append("GRID \n");
        cTemp.append("GROUP BY DEALERCODE, DEALER \n");
        cTemp.append("ORDER BY DEALER \n");


        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getTotalSalesFrotasSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType) {

        StringBuilder retValue = new StringBuilder();
        retValue.append(  "WITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");

        StringBuilder cTemp = new StringBuilder();

        cTemp.append("SUM(M0) AS M0, \n");
        cTemp.append("SUM(D1) AS D1, \n");
        cTemp.append("SUM(PLT0) AS PLT0, \n");
        cTemp.append("SUM(M1) AS M1, \n");
        cTemp.append("SUM(D2) AS D2, \n");
        cTemp.append("SUM(PLT1) AS PLT1, \n");
        cTemp.append("SUM(M2) AS M2, \n");
        cTemp.append("SUM(PLT2) AS PLT2 \n");
        cTemp.append("FROM \n");
        cTemp.append("( \n");
        cTemp.append("   SELECT \n");
        cTemp.append("   D.OID_DEALER, \n");
        cTemp.append("	 D.SUB_DEALER,  \n");
        cTemp.append("   D.DEALERCODE, \n");
        cTemp.append("   D.DEALER, \n");
        cTemp.append("   M.ID AS ID_PVM_CARMODEL, \n");
        cTemp.append("   M.TYPE, \n");
        cTemp.append("   PMRD0.SALES_VALUE AS M0, \n");
        cTemp.append("   PMRD0.PLATES_VALUE AS PLT0, \n");
        cTemp.append("   PMRD0.SALES_VALUE - PMRD1.SALESD1 AS D1, \n");
        cTemp.append("   PMRD0.SALES_VALUE2 AS M1, \n");
        cTemp.append("   PMRD0.PLATES_VALUE2 AS PLT1, \n");
        cTemp.append("   PMRD0.SALES_VALUE2 - PMRD1.SALESD2 AS D2, \n");
        cTemp.append("   PMRD0.SALES_VALUE3 AS M2, \n");
        cTemp.append("   PMRD0.PLATES_VALUE3 AS PLT2 \n");
        cTemp.append("   FROM DEALERS D \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      * \n");
        cTemp.append("      FROM PVM_CARMODEL \n");
        cTemp.append("      WHERE ACTIVE = 'S' \n");
        cTemp.append("      AND TYPE = '" + modelType + "' \n");
        cTemp.append("      AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM \n");
        cTemp.append("      AND VALUE(DT_TO, DATE('2099-12-31')) \n");
        cTemp.append("   ) \n");
        cTemp.append("   M ON 1=1 \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      PMR.OID_DEALER, \n");
        cTemp.append("      PMR.SUB_DEALER, \n");
        cTemp.append("      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append("      PMRD.SALES_VALUE, \n");
        cTemp.append("      PMRD.SALES_VALUE2, \n");
        cTemp.append("      PMRD.SALES_VALUE3, \n");
        cTemp.append("      PMRD.PLATES_VALUE, \n");
        cTemp.append("      PMRD.PLATES_VALUE2, \n");
        cTemp.append("      PMRD.PLATES_VALUE3 \n");
        cTemp.append("      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append("      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        cTemp.append("      WHERE PMR.YEAR = " + year + " \n");
        cTemp.append("      AND PMR.MONTH = " + month + " \n");
        cTemp.append("   ) \n");
        cTemp.append("   PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER \n");
        cTemp.append("   AND D.SUB_DEALER = PMRD0.SUB_DEALER \n");
        cTemp.append("   AND PMRD0.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      PMR.OID_DEALER, \n");
        cTemp.append("      PMR.SUB_DEALER, \n");
        cTemp.append("      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append("      PMRD.SALES_VALUE2 AS SALESD1, \n");
        cTemp.append("      PMRD.SALES_VALUE3 AS SALESD2 \n");
        cTemp.append("      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append("      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");

        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append("      WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append("      AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append("   ) \n");
        cTemp.append("   PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER \n");
        cTemp.append("   AND D.SUB_DEALER = PMRD1.SUB_DEALER \n");
        cTemp.append("   AND PMRD1.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append(") \n");
        cTemp.append("GRID \n");
        cTemp.append("GROUP BY DEALERCODE, DEALER \n");
        cTemp.append("ORDER BY DEALER \n");


        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getVDVCSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month, String modelType) {
        StringBuilder retValue = new StringBuilder();
        Iterator<String[]> iterVec = null;
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );


        for (RetailDealerDTO retailDealerAct :vecRetailerDealers) {
            retValue.append(  "('" + retailDealerAct.getObjectid() + "', '" + retailDealerAct.getDealerCode() + "', '" + retailDealerAct.getDesig() + "'), \n"  );
        }

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");


        StringBuilder cTemp = new StringBuilder();

        for (String[] model :vecPVMCarModel) {
            cTemp.append(makeQueryFromModel(model));
        }

        cTemp.append("'' AS DUMMY \n");

        cTemp.append(  "FROM ( \n");
        cTemp.append(  "   SELECT \n");
        cTemp.append(  "   D.OID_DEALER, \n");
        cTemp.append(  "   D.DEALERCODE, \n");
        cTemp.append(  "   D.DEALER, \n");
        cTemp.append(  "   M.ID AS ID_PVM_CARMODEL, \n");
        cTemp.append(  "   M.TYPE, \n");
        cTemp.append(  "   PMRD0.VDVC AS M0, \n");
        cTemp.append(  "   PMRD0.VDVC - PMRD1.VDVCD1  AS D1, \n");
        cTemp.append(  "   PMRD0.VDVC2 AS M1, \n");
        cTemp.append(  "   PMRD0.VDVC2 - PMRD1.VDVCD2 AS D2, \n");
        cTemp.append(  "   PMRD0.VDVC3 AS M2 \n");
        cTemp.append(  "   FROM DEALERS D \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   (SELECT * FROM PVM_CARMODEL WHERE ACTIVE = 'S' AND TYPE = '" + modelType + "' AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM AND VALUE(DT_TO, DATE('2099-12-31'))) M ON 1=1 \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(  "      PMR.OID_DEALER, \n");
        cTemp.append(  "      PMR.SUB_DEALER, \n");
        cTemp.append(  "      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append(  "      PMRD.VDVC, \n");
        cTemp.append(  "      PMRD.VDVC2, \n");
        cTemp.append(  "      PMRD.VDVC3 \n");
        cTemp.append(  "      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append(  "      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        cTemp.append(  "      WHERE PMR.YEAR = " + year + " \n");
        cTemp.append(  "      AND PMR.MONTH = " + month + " \n");
        cTemp.append(  "   ) PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER AND PMRD0.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append(  "   LEFT JOIN \n");
        cTemp.append(  "   ( \n");
        cTemp.append(  "      SELECT \n");
        cTemp.append(  "      PMR.OID_DEALER, \n");
        cTemp.append(  "      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append(  "      PMRD.VDVC2 AS VDVCD1, \n");
        cTemp.append(  "      PMRD.VDVC3 AS VDVCD2 \n");
        cTemp.append(  "      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append(  "      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append(  "      WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append(  "      AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append(  "   ) PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER AND PMRD1.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append(  ") GRID \n");
        cTemp.append(  "GROUP BY DEALERCODE, DEALER \n");
        cTemp.append(  "ORDER BY DEALER \n");

        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String getTotalVDVCSql(List<RetailDealerDTO> vecRetailerDealers, String year, String month, String modelType) {
        StringBuffer retValue = new StringBuffer();
        Iterator<String[]> iterVec = null;
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO retailDealerAct :vecRetailerDealers) {
            retValue.append(  "('" + retailDealerAct.getObjectid() + "', '" + retailDealerAct.getDealerCode() + "', '" + retailDealerAct.getDesig() + "'), \n"  );
        }

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( ")\nSELECT DEALERCODE AS \"Cód Concessionário\",\nDEALER AS \"Concessionário\", \n");

        StringBuffer cTemp = new StringBuffer();

        cTemp.append("SUM(M0) AS M0, \n");
        cTemp.append("SUM(D1) AS D1, \n");
        cTemp.append("SUM(M1) AS M1, \n");
        cTemp.append("SUM(D2) AS D2, \n");
        cTemp.append("SUM(M2) AS M2 \n");
        cTemp.append("FROM \n");
        cTemp.append("( \n");
        cTemp.append("   SELECT \n");
        cTemp.append("   D.OID_DEALER, \n");
        cTemp.append("   D.DEALERCODE, \n");
        cTemp.append("   D.DEALER, \n");
        cTemp.append("   M.ID AS ID_PVM_CARMODEL, \n");
        cTemp.append("   M.TYPE, \n");
        cTemp.append("   PMRD0.VDVC AS M0, \n");
        cTemp.append("   PMRD0.VDVC - PMRD1.VDVCD1 AS D1, \n");
        cTemp.append("   PMRD0.VDVC2 AS M1, \n");
        cTemp.append("   PMRD0.VDVC2 - PMRD1.VDVCD2 AS D2, \n");
        cTemp.append("   PMRD0.VDVC3 AS M2 \n");
        cTemp.append("   FROM DEALERS D \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      * \n");
        cTemp.append("      FROM PVM_CARMODEL \n");
        cTemp.append("      WHERE ACTIVE = 'S' \n");
        cTemp.append("      AND TYPE = '" + modelType + "' \n");
        cTemp.append("      AND DATE('" + year + "-" + month + "-01') BETWEEN DT_FROM \n");
        cTemp.append("      AND VALUE(DT_TO, DATE('2099-12-31')) \n");
        cTemp.append("   ) \n");
        cTemp.append("   M ON 1=1 \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      PMR.OID_DEALER, \n");
        cTemp.append("      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append("      PMRD.VDVC, \n");
        cTemp.append("      PMRD.VDVC2, \n");
        cTemp.append("      PMRD.VDVC3 \n");
        cTemp.append("      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append("      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");
        cTemp.append("      WHERE PMR.YEAR = " + year + " \n");
        cTemp.append("      AND PMR.MONTH = " + month + " \n");
        cTemp.append("   ) \n");
        cTemp.append("   PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER \n");
        cTemp.append("   AND PMRD0.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append("   LEFT JOIN \n");
        cTemp.append("   ( \n");
        cTemp.append("      SELECT \n");
        cTemp.append("      PMR.OID_DEALER, \n");
        cTemp.append("      PMRD.ID_PVM_CARMODEL, \n");
        cTemp.append("      PMRD.VDVC2 AS VDVCD1, \n");
        cTemp.append("      PMRD.VDVC3 AS VDVCD2 \n");
        cTemp.append("      FROM PVM_MONTHLYREPORTDETAIL PMRD \n");
        cTemp.append("      INNER JOIN PVM_MONTHLYREPORT PMR ON PMRD.ID_PVM_MONTHLYREPORT = PMR.ID \n");

        //Retirar 1 m�s � data
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
        cal.add(Calendar.MONTH, -1);

        String yearTMP 	= String.valueOf(cal.get(Calendar.YEAR));
        String monthTMP = String.valueOf(cal.get(Calendar.MONTH) + 1);

        cTemp.append("      WHERE PMR.YEAR = " + yearTMP + " \n");
        cTemp.append("      AND PMR.MONTH = " + monthTMP + " \n");
        cTemp.append("   ) \n");
        cTemp.append("   PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER \n");
        cTemp.append("   AND PMRD1.ID_PVM_CARMODEL = M.ID \n");
        cTemp.append(") \n");
        cTemp.append("GRID \n");
        cTemp.append("GROUP BY DEALERCODE, DEALER \n");
        cTemp.append("ORDER BY DEALER \n");


        retValue.append(cTemp);

        return retValue.toString();
    }

    public static String makeSubQuery1(boolean isSalesFrotas){
        if(isSalesFrotas)
            return   "WITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS  \n";

        return  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS  \n";
    }

    public static String makeSubQuery2(boolean isSalesFrotas, List<RetailDealerDTO> vecRetailerDealers) {
        StringBuilder retValue = new StringBuilder();
        if(isSalesFrotas) {
            retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
            retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
            retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
            retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );
        }else {
            for (RetailDealerDTO retailDealerAct :vecRetailerDealers) {
                retValue.append(  "('" + retailDealerAct.getObjectid() + "', '" + retailDealerAct.getDealerCode() + "', '" + retailDealerAct.getDesig() + "'), \n"  );
            }
        }
        return retValue.toString();
    }

    public static String makeSubQuery3(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return  "   D.SUB_DEALER, \n";
        return "";
    }

    public static String makeSubQuery4(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return   "      PMR.SUB_DEALER, \n";
        return "";
    }

    public static String makeSubQuery5(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return  "   ) PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER AND D.SUB_DEALER = PMRD0.SUB_DEALER AND PMRD0.ID_PVM_CARMODEL = M.ID \n";

        return  "   ) PMRD0 ON D.OID_DEALER = PMRD0.OID_DEALER AND PMRD0.ID_PVM_CARMODEL = M.ID \n";
    }


    public static String makeSubQuery6(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return  "   ) PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER AND D.SUB_DEALER = PMRD1.SUB_DEALER AND PMRD1.ID_PVM_CARMODEL = M.ID \n";

        return  "   ) PMRD1 ON D.OID_DEALER = PMRD1.OID_DEALER AND PMRD1.ID_PVM_CARMODEL = M.ID \n";
    }

    public static String makeSubQuery7(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return    "      PB.OID_DEALER, PB.SUB_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC \n";

        return  "   PB.OID_DEALER, PB.ID_PVM_CARMODEL, PB.PLATES, 1 AS ORC \n";
    }

    public static String makeSubQuery8(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return    "   ) PB0 ON D.OID_DEALER = PB0.OID_DEALER AND D.SUB_DEALER = PB0.SUB_DEALER AND PB0.ID_PVM_CARMODEL = M.ID \n";

        return  "   ) PB0 ON D.OID_DEALER = PB0.OID_DEALER AND PB0.ID_PVM_CARMODEL = M.ID \n";
    }

    public static String makeSubQuery9(boolean isSalesFrotas) {
        return makeSubQuery7(isSalesFrotas);
    }

    public static String makeSubQuery10(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return    "   ) PB1 ON D.OID_DEALER = PB1.OID_DEALER AND D.SUB_DEALER = PB1.SUB_DEALER AND PB1.ID_PVM_CARMODEL = M.ID \n";

        return  "   ) PB1 ON D.OID_DEALER = PB1.OID_DEALER AND PB1.ID_PVM_CARMODEL = M.ID \n";
    }

    public static String makeSubQuery11(boolean isSalesFrotas) {
        return makeSubQuery7(isSalesFrotas);
    }

    public static String makeSubQuery12(boolean isSalesFrotas) {
        if(isSalesFrotas)
            return    "   ) PB2 ON D.OID_DEALER = PB2.OID_DEALER AND D.SUB_DEALER = PB2.SUB_DEALER AND PB2.ID_PVM_CARMODEL = M.ID \n";

        return  "   ) PB2 ON D.OID_DEALER = PB2.OID_DEALER AND PB2.ID_PVM_CARMODEL = M.ID \n";
    }

    public static String makeQueryFromModel(String[] model) {
        StringBuilder cTemp = new StringBuilder();
        switch (model[1].substring(0, 3)) {
            case "M1-":
                cTemp.append("SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN M0 ELSE NULL END) AS  \"" + model[1] + "\",\n");
                break;
            case "D1-":
                cTemp.append("SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN D1 ELSE NULL END) AS  \"" + model[1] + "\",\n");
                break;
            case "M2-":
                cTemp.append("SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN M1 ELSE NULL END) AS \"" + model[1] + "\",\n");
                break;
            case "D2-":
                cTemp.append("SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN D2 ELSE NULL END) AS \"" + model[1] + "\",\n");
                break;
            case "M3-":
                cTemp.append("SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN M2 ELSE NULL END) AS \"" + model[1] + "\",\n");
                break;
        }

        return cTemp.toString();
    }

}
