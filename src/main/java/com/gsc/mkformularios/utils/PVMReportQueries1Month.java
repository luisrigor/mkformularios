package com.gsc.mkformularios.utils;

import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.rg.dealer.Dealer;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class PVMReportQueries1Month {

    public static String getPlatesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO currentRetailer: vecRetailerDealers) {
            retValue.append(  "('" + currentRetailer.getObjectid() + "', '" + currentRetailer.getDealerCode() + "', '" + currentRetailer.getDesig() + "'), \n"  );
        }

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( "),PRV (ORDINAL, \"Cód Concessionário\", \"Concessionário\",");

//-------------- HEADERS ---------------------------------------------------------------------

        String oldType = "";
        StringBuffer columnHeaders = new StringBuffer();
        StringBuffer shortHeaders = new StringBuffer();
        columnHeaders.append("ORDINAL, \"Cód Concessionário\", \"Concessionário\",");
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    columnHeaders.append(  "\"Total " + oldType + "\", " );
                    columnHeaders.append(  "\"Total Orç. " + oldType + "\", " );

                    shortHeaders.append(  "\"Total " + oldType + "\", " );
                    shortHeaders.append(  "\"Total Orç. " + oldType + "\", " );

                    oldType =  model[0] ;
                }
            }
            columnHeaders.append(  "\"" + model[1] + "\", " );
            shortHeaders.append(  "\"" + model[1] + "\", " );
            oldType =  model[0] ;
        }

        columnHeaders.append(  "\"Total " + oldType + "\", " );
        columnHeaders.append(  "\"Total Orç. " + oldType + "\", " );
        columnHeaders.append(  "\"Total Matrículas\", " );
        columnHeaders.append(  "\"Total Orç.\", " );
        columnHeaders.append(  "\"Var.%\"");

        shortHeaders.append(  "\"Total " + oldType + "\", " );
        shortHeaders.append(  "\"Total Orç. " + oldType + "\", " );
        shortHeaders.append(  "\"Total Matrículas\", " );
        shortHeaders.append(  "\"Total Orç.\", " );
        shortHeaders.append(  "\"Var.%\"");

//---------------------------------------------------------------------------------------------

        retValue.append(shortHeaders);
        retValue.append(  ") AS (\n");
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\",DEALERS.DEALER AS \"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append(" FROM DEALERS INNER JOIN ");
        retValue.append("(SELECT ");
        retValue.append("PVM.OID_DEALER, ");

        oldType = "";
        StringBuffer cTemp = new StringBuffer();
        StringBuffer cTemp2 = new StringBuffer();
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    cTemp.append(  "0 AS \"Total " + oldType + "\", \n");
//		    		cTemp.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES_VALUE ELSE NULL END) AS \"Total " + oldType + "\", \n");
                    cTemp.append(  "0 AS \"Total Orç. " + oldType + "\", \n");
//		    		cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total " + oldType + "\", \n");
                    cTemp2.append(  "0 AS \"Total " + oldType + "\", \n");
                    cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total Orç. " + oldType + "\", \n");
                    oldType =  model[0] ;
                }
            }
            cTemp.append(  "SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN PLATES_VALUE ELSE NULL END) AS \"" + model[1] + "\", \n" );
            cTemp2.append(  "SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN PLATES ELSE NULL END) AS \"" + model[1] + "\", \n" );
            oldType =  model[0] ;
        }

        cTemp.append(  "0 AS \"Total " + oldType + "\", \n");
        cTemp.append(  "0 AS \"Total Orç. " + oldType + "\", \n");
        cTemp2.append(  "0 AS \"Total " + oldType + "\", \n");
        cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total Orç. " + oldType + "\", \n");

        cTemp.append(  "SUM(PLATES_VALUE) AS \"Total Matrículas\", \n");
        cTemp.append(  "0 AS \"Total Orç.\", \n");
        cTemp.append(  "SUM(0) as \"Var.%\" \n");
        cTemp2.append(  "SUM(PLATES) AS \"Total Matrículas\", \n");
        cTemp2.append(  "SUM(PLATES) AS \"Total Orç.\", \n");
        cTemp2.append(  "0 as \"Var.%\" \n");

        retValue.append(cTemp);

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE \n");

        retValue.append("WHERE\n");
        retValue.append("PVM.AVAILABLE = 1 AND \n");
        retValue.append("PVM.YEAR = " + year + " AND \n");
        retValue.append("PVM.MONTH = " + month + " AND \n");
        retValue.append("PVM.ID = DT.ID_PVM_MONTHLYREPORT AND \n");
        //retValue.append("CM.ACTIVE = 'S' AND \n");
        retValue.append("DT.ID_PVM_CARMODEL = CM.ID AND \n");
        retValue.append("PVM.OID_DEALER = DE.OID_DEALER \n");
        retValue.append("GROUP BY PVM.OID_DEALER) DETAIL ON \n");
        retValue.append("DEALERS.OID_DEALER = DETAIL.OID_DEALER \n");
        retValue.append("), ORC (ORDINAL, \"Cód Concessionário\",\"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append(") AS (\n");
        retValue.append("SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append("FROM DEALERS left outer JOIN\n");
        retValue.append("(SELECT \n");
        retValue.append("PVM.OID_DEALER, \n");
        retValue.append(cTemp2);

        retValue.append("FROM PVM_BUDGET AS PVM, PVM_CARMODEL AS CM, DEALERS AS DE \n");
        retValue.append("WHERE \n");
        retValue.append("PVM.YEAR = " + year + " AND \n");
        retValue.append("PVM.MONTH = " + month + " AND \n");
        retValue.append("PVM.OID_DEALER = DE.OID_DEALER AND \n");
        retValue.append("PVM.ID_PVM_CARMODEL = CM.ID \n");
        retValue.append("GROUP BY PVM.OID_DEALER) DETAIL \n");
        retValue.append("ON \n");
        retValue.append("DEALERS.OID_DEALER = DETAIL.OID_DEALER \n");
        retValue.append(") \n");



//		-------------------------------------------------------------------------------------------
        oldType = "";
        StringBuffer lastHeaders = new StringBuffer();
        lastHeaders.append("SELECT PRV.\"Cód Concessionário\", PRV.\"Concessionário\", \n");
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    lastHeaders.append(  "PRV." + "\"Total " + oldType + "\", \n" );
                    lastHeaders.append(  "ORC." + "\"Total Orç. " + oldType + "\", \n" );
                    oldType =  model[0] ;
                }
            }
            lastHeaders.append(  "PRV." + "\"" + model[1] + "\", " );
            oldType =  model[0] ;
        }


        lastHeaders.append(  "PRV." + "\"Total " + oldType + "\", \n" );
        lastHeaders.append(  "ORC." + "\"Total Orç. " + oldType + "\", \n" );
        lastHeaders.append(  "PRV." + "\"Total Matrículas\", \n" );
        lastHeaders.append(  "ORC." + "\"Total Orç.\", \n" );
        lastHeaders.append(  "PRV." + "\"Var.%\", ");

        lastHeaders.append("' ' AS DUMMY ");
        lastHeaders.append("FROM PRV, ORC WHERE PRV.\"Concessionário\" = ORC.\"Concessionário\"\n");
        retValue.append(lastHeaders);

//---------------------------------------------------------------------------------------------
        retValue.append(  "UNION\n");

        retValue.append("SELECT \"Cód Concessionário\",\"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append(", '*' AS DUMMY\n");
        retValue.append("FROM ORC WHERE \"Concessionário\" NOT IN (SELECT DISTINCT DEALERS.DEALER FROM PVM_MONTHLYREPORT, DEALERS WHERE PVM_MONTHLYREPORT.OID_DEALER = DEALERS.OID_DEALER AND PVM_MONTHLYREPORT.YEAR = " + year + " and PVM_MONTHLYREPORT.MONTH = " + month + " and AVAILABLE = 1)");
        retValue.append(" ORDER BY \"Concessionário\"");


        return retValue.toString();
    }

    /*
     * Separa Gaia Frotas de Gaia Imobilizado
     */
    public static String getFrotasPlatesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "WITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );

        retValue.append(  "('"+ Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );

        retValue.deleteCharAt(retValue.length() -3);

        retValue.append( "),PRV (SUB_DEALER, ORDINAL, \"Cód Concessionário\", \"Concessionário\",");

//-------------- HEADERS ---------------------------------------------------------------------

        String oldType = "";
        StringBuffer columnHeaders = new StringBuffer();
        StringBuffer shortHeaders = new StringBuffer();
        columnHeaders.append("ORDINAL, \"Cód Concessionário\", \"Concessionário\",");
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    columnHeaders.append(  "\"Total " + oldType + "\", " );
                    columnHeaders.append(  "\"Total Orç. " + oldType + "\", " );

                    shortHeaders.append(  "\"Total " + oldType + "\", " );
                    shortHeaders.append(  "\"Total Orç. " + oldType + "\", " );

                    oldType =  model[0] ;
                }
            }
            columnHeaders.append(  "\"" + model[1] + "\", " );
            shortHeaders.append(  "\"" + model[1] + "\", " );
            oldType =  model[0] ;
        }

        columnHeaders.append(  "\"Total " + oldType + "\", " );
        columnHeaders.append(  "\"Total Orç. " + oldType + "\", " );
        columnHeaders.append(  "\"Total Matrículas\", " );
        columnHeaders.append(  "\"Total Orç.\", " );
        columnHeaders.append(  "\"Var.%\"");

        shortHeaders.append(  "\"Total " + oldType + "\", " );
        shortHeaders.append(  "\"Total Orç. " + oldType + "\", " );
        shortHeaders.append(  "\"Total Matrículas\", " );
        shortHeaders.append(  "\"Total Orç.\", " );
        shortHeaders.append(  "\"Var.%\"");

//---------------------------------------------------------------------------------------------

        retValue.append(shortHeaders);
        retValue.append(  ") AS (\n");
        retValue.append(  "SELECT DEALERS.SUB_DEALER, '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\",DEALERS.DEALER AS \"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append(" FROM DEALERS INNER JOIN ");
        retValue.append("(SELECT ");
        retValue.append("PVM.OID_DEALER, PVM.SUB_DEALER, ");

        oldType = "";
        StringBuffer cTemp = new StringBuffer();
        StringBuffer cTemp2 = new StringBuffer();
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    cTemp.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES_VALUE ELSE NULL END) AS \"Total " + oldType + "\", \n");
                    cTemp.append(  "0 AS \"Total Orç. " + oldType + "\", \n");
                    cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total " + oldType + "\", \n");
                    cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total Orç. " + oldType + "\", \n");
                    oldType =  model[0] ;
                }
            }
            cTemp.append(  "SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN PLATES_VALUE ELSE NULL END) AS \"" + model[1] + "\", \n" );
            cTemp2.append(  "SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN PLATES ELSE NULL END) AS \"" + model[1] + "\", \n" );
            oldType =  model[0] ;
        }

        cTemp.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES_VALUE ELSE NULL END) AS \"Total " + oldType + "\", \n");
        cTemp.append(  "0 AS \"Total Orç. " + oldType + "\", \n");
        cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total " + oldType + "\", \n");
        cTemp2.append(  "SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN PLATES ELSE NULL END) AS \"Total Orç. " + oldType + "\", \n");

        cTemp.append(  "SUM(PLATES_VALUE) AS \"Total Matrículas\", \n");
        cTemp.append(  "0 AS \"Total Orç.\", \n");
        cTemp.append(  "SUM(0) as \"Var.%\" \n");
        cTemp2.append(  "SUM(PLATES) AS \"Total Matrículas\", \n");
        cTemp2.append(  "SUM(PLATES) AS \"Total Orç.\", \n");
        cTemp2.append(  "0 as \"Var.%\" \n");

        retValue.append(cTemp);

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE \n");

        retValue.append("WHERE\n");
        retValue.append("PVM.AVAILABLE = 1 AND \n");
        retValue.append("PVM.YEAR = " + year + " AND \n");
        retValue.append("PVM.MONTH = " + month + " AND \n");
        retValue.append("PVM.ID = DT.ID_PVM_MONTHLYREPORT AND \n");
        retValue.append("DT.ID_PVM_CARMODEL = CM.ID AND \n");
        retValue.append("PVM.OID_DEALER = DE.OID_DEALER AND \n");
        retValue.append("PVM.SUB_DEALER = DE.SUB_DEALER \n");
        retValue.append("GROUP BY PVM.OID_DEALER, PVM.SUB_DEALER) DETAIL ON \n");
        retValue.append("DEALERS.OID_DEALER = DETAIL.OID_DEALER AND DEALERS.SUB_DEALER = DETAIL.SUB_DEALER\n");
        retValue.append("), ORC (SUB_DEALER, ORDINAL, \"Cód Concessionário\",\"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append(") AS (\n");
        retValue.append("SELECT DEALERS.SUB_DEALER, '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append("FROM DEALERS left outer JOIN\n");
        retValue.append("(SELECT \n");
        retValue.append("PVM.OID_DEALER, PVM.SUB_DEALER,\n");
        retValue.append(cTemp2);

        retValue.append("FROM PVM_BUDGET AS PVM, PVM_CARMODEL AS CM, DEALERS AS DE \n");
        retValue.append("WHERE \n");
        retValue.append("PVM.YEAR = " + year + " AND \n");
        retValue.append("PVM.MONTH = " + month + " AND \n");
        retValue.append("PVM.OID_DEALER = DE.OID_DEALER AND \n");
        retValue.append("PVM.SUB_DEALER = DE.SUB_DEALER AND \n");
        retValue.append("PVM.ID_PVM_CARMODEL = CM.ID \n");
        retValue.append("GROUP BY PVM.OID_DEALER, PVM.SUB_DEALER) DETAIL \n");
        retValue.append("ON \n");
        retValue.append("DEALERS.OID_DEALER = DETAIL.OID_DEALER AND DEALERS.SUB_DEALER = DETAIL.SUB_DEALER\n");
        retValue.append(") \n");



//		-------------------------------------------------------------------------------------------
        oldType = "";
        StringBuffer lastHeaders = new StringBuffer();
        lastHeaders.append("SELECT PRV.\"Cód Concessionário\", PRV.\"Concessionário\", \n");
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    lastHeaders.append(  "PRV." + "\"Total " + oldType + "\", \n" );
                    lastHeaders.append(  "ORC." + "\"Total Orç. " + oldType + "\", \n" );
                    oldType =  model[0] ;
                }
            }
            lastHeaders.append(  "PRV." + "\"" + model[1] + "\", " );
            oldType =  model[0] ;
        }


        lastHeaders.append(  "PRV." + "\"Total " + oldType + "\", \n" );
        lastHeaders.append(  "ORC." + "\"Total Orç. " + oldType + "\", \n" );
        lastHeaders.append(  "PRV." + "\"Total Matrículas\", \n" );
        lastHeaders.append(  "ORC." + "\"Total Orç.\", \n" );
        lastHeaders.append(  "PRV." + "\"Var.%\", ");

        lastHeaders.append("' ' AS DUMMY ");
        lastHeaders.append("FROM PRV, ORC WHERE PRV.\"Concessionário\" = ORC.\"Concessionário\" AND PRV.SUB_DEALER = ORC.SUB_DEALER\n");
        retValue.append(lastHeaders);

//---------------------------------------------------------------------------------------------
        retValue.append(  "UNION\n");

        retValue.append("SELECT \"Cód Concessionário\",\"Concessionário\", ");
        retValue.append(shortHeaders);
        retValue.append(", '*' AS DUMMY\n");
        retValue.append("FROM ORC WHERE (\"Concessionário\", SUB_DEALER) NOT IN (SELECT DISTINCT DEALERS.DEALER, DEALERS.SUB_DEALER FROM PVM_MONTHLYREPORT, DEALERS WHERE PVM_MONTHLYREPORT.OID_DEALER = DEALERS.OID_DEALER AND PVM_MONTHLYREPORT.SUB_DEALER = DEALERS.SUB_DEALER AND PVM_MONTHLYREPORT.YEAR = " + year + " and PVM_MONTHLYREPORT.MONTH = " + month + " and AVAILABLE = 1)");
        retValue.append(" ORDER BY \"Concessionário\"");

        return retValue.toString();
    }

    public static String getVDVCSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month){
        StringBuffer retValue = new StringBuffer();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO currentRetailer: vecRetailerDealers) {
            retValue.append(  "('" + currentRetailer.getObjectid() + "', '" + currentRetailer.getDealerCode() + "', '" + currentRetailer.getDesig() + "'), \n"  );
        }

        //retValue.append(  "('SC00020001', '75', '[Gaia (Sede) / Frotas]'), \n"  );

        retValue.deleteCharAt(retValue.length() -3);
        retValue.append(  ")\n" );
        retValue.append(  "SELECT * FROM (\n" );
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\"," );

        String oldType = "";
        StringBuffer cTemp = new StringBuffer();
        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    retValue.append(  "\"Total VDVC " + oldType + "\", " );
                    cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN VDVC ELSE NULL END) AS \"Total VDVC " + oldType + "\",\n" );
                    oldType =  model[0] ;
                }
            }
            retValue.append(  "\"" + model[1] + "\", " );
            cTemp.append(  "    SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN VDVC ELSE NULL END) AS \"" + model[1] + "\",\n" );
            oldType =  model[0] ;
        }
        retValue.append(  "\"Total VDVC " + oldType + "\", " );
        cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN VDVC ELSE NULL END) AS \"Total VDVC " + oldType + "\",\n" );

        retValue.append(  "\"Total VDVC\"\n" );
        cTemp.append(  "    SUM(VDVC) AS \"Total VDVC\" \n");

        retValue.append(  "FROM DEALERS LEFT OUTER JOIN\n" );
        retValue.append(  "(SELECT\n" );
        retValue.append(  "    PVM.OID_DEALER,\n" );
        retValue.append(  cTemp );

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE\n");
        retValue.append(  "WHERE\n");
        retValue.append(  "    PVM.AVAILABLE = 1 AND\n");
        retValue.append(  "    PVM.YEAR = " + year + " AND\n");
        retValue.append(  "    PVM.MONTH = " + month + " AND\n");
        retValue.append(  "    PVM.ID = DT.ID_PVM_MONTHLYREPORT AND\n");
        retValue.append(  "    DT.ID_PVM_CARMODEL = CM.ID AND\n");
        retValue.append(  "    PVM.OID_DEALER = DE.OID_DEALER\n");
        retValue.append(  "    GROUP BY PVM.OID_DEALER) DETAIL ON\n");
        retValue.append(  "    DEALERS.OID_DEALER = DETAIL.OID_DEALER\n");
        retValue.append(  ") GRID ORDER BY ORDINAL, \"Concessionário\"");

        return retValue.toString();
    }

    public static String getSalesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO currentRetailer: vecRetailerDealers) {
            retValue.append(  "('" + currentRetailer.getObjectid() + "', '" + currentRetailer.getDealerCode() + "', '" + currentRetailer.getDesig() + "'), \n"  );
        }

        retValue.deleteCharAt(retValue.length() -3);
        retValue.append(  ")\n" );
        retValue.append(  "SELECT * FROM (\n" );
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\"," );

        String oldType = "";
        StringBuffer cTemp = new StringBuffer();

        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    retValue.append(  "\"Total Vendas " + oldType + "\", " );
                    cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN SALES_VALUE ELSE NULL END) AS \"Total Vendas " + oldType + "\",\n" );
                    oldType =  model[0] ;
                }
            }
            retValue.append(  "\"" + model[1] + "\", " );
            cTemp.append(  "    SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN SALES_VALUE ELSE NULL END) AS \"" + model[1] + "\",\n" );
            oldType =  model[0] ;
        }

        retValue.append(  "\"Total Vendas " + oldType + "\", " );
        cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN SALES_VALUE ELSE NULL END) AS \"Total Vendas " + oldType + "\",\n" );

        retValue.append(  "\"Total Vendas\", " );
        cTemp.append(  "    SUM(SALES_VALUE) AS \"Total Vendas\",\n");

        retValue.append(  "\"Total Matrículas\", " );
        cTemp.append(  "    SUM(PLATES_VALUE) AS \"Total Matrículas\",\n");

        retValue.append(  "\"Dif\"\n" );
        cTemp.append(  "    SUM(SALES_VALUE - PLATES_VALUE) as \"Dif\"\n");

        retValue.append(  "FROM DEALERS LEFT OUTER JOIN\n" );
        retValue.append(  "(SELECT\n" );
        retValue.append(  "    PVM.OID_DEALER,\n" );
        retValue.append(  cTemp );

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE\n");
        retValue.append(  "WHERE\n");
        retValue.append(  "    PVM.AVAILABLE = 1 AND\n");
        retValue.append(  "    PVM.YEAR = " + year + " AND\n");
        retValue.append(  "    PVM.MONTH = " + month + " AND\n");
        retValue.append(  "    PVM.ID = DT.ID_PVM_MONTHLYREPORT AND\n");
        retValue.append(  "    DT.ID_PVM_CARMODEL = CM.ID AND\n");
        retValue.append(  "    PVM.OID_DEALER = DE.OID_DEALER\n");
        retValue.append(  "    GROUP BY PVM.OID_DEALER) DETAIL ON\n");
        retValue.append(  "    DEALERS.OID_DEALER = DETAIL.OID_DEALER\n");
        retValue.append(  ") GRID ORDER BY ORDINAL, \"Concessionário\"");

        return retValue.toString();
    }

    public static String getFrotasSalesSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS \n"  );
        retValue.append(  "(VALUES\n" );

        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );

        retValue.deleteCharAt(retValue.length() -3);
        retValue.append(  ")\n" );
        retValue.append(  "SELECT * FROM (\n" );
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\"," );

        String oldType = "";
        StringBuffer cTemp = new StringBuffer();

        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    retValue.append(  "\"Total Vendas " + oldType + "\", " );
                    cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN SALES_VALUE ELSE NULL END) AS \"Total Vendas " + oldType + "\",\n" );
                    oldType =  model[0] ;
                }
            }
            retValue.append(  "\"" + model[1] + "\", " );
            cTemp.append(  "    SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN SALES_VALUE ELSE NULL END) AS \"" + model[1] + "\",\n" );
            oldType =  model[0] ;
        }

        retValue.append(  "\"Total Vendas " + oldType + "\", " );
        cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN SALES_VALUE ELSE NULL END) AS \"Total Vendas " + oldType + "\",\n" );

        retValue.append(  "\"Total Vendas\", " );
        cTemp.append(  "    SUM(SALES_VALUE) AS \"Total Vendas\",\n");

        retValue.append(  "\"Total Matrículas\", " );
        cTemp.append(  "    SUM(PLATES_VALUE) AS \"Total Matrículas\",\n");

        retValue.append(  "\"Dif\"\n" );
        cTemp.append(  "    SUM(SALES_VALUE - PLATES_VALUE) as \"Dif\"\n");

        retValue.append(  "FROM DEALERS LEFT OUTER JOIN\n" );
        retValue.append(  "(SELECT\n" );
        retValue.append(  "    PVM.OID_DEALER,PVM.SUB_DEALER,\n" );
        retValue.append(  cTemp );

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE\n");
        retValue.append(  "WHERE\n");
        retValue.append(  "    PVM.AVAILABLE = 1 AND\n");
        retValue.append(  "    PVM.YEAR = " + year + " AND\n");
        retValue.append(  "    PVM.MONTH = " + month + " AND\n");
        retValue.append(  "    PVM.ID = DT.ID_PVM_MONTHLYREPORT AND\n");
        retValue.append(  "    DT.ID_PVM_CARMODEL = CM.ID AND\n");
        retValue.append(  "    PVM.OID_DEALER = DE.OID_DEALER\n");
        retValue.append(  "    AND PVM.SUB_DEALER = DE.SUB_DEALER \n");
        retValue.append(  "    GROUP BY PVM.OID_DEALER, PVM.SUB_DEALER) DETAIL ON\n");
        retValue.append(  "    DEALERS.OID_DEALER = DETAIL.OID_DEALER AND DEALERS.SUB_DEALER = DETAIL.SUB_DEALER\n");

        retValue.append(  ") GRID ORDER BY ORDINAL, \"Concessionário\"");

        return retValue.toString();
    }

    public static String getContractsSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER) AS \n"  );
        retValue.append(  "(VALUES\n" );

        for (RetailDealerDTO currentRetailer: vecRetailerDealers) {
            retValue.append(  "('" + currentRetailer.getObjectid() + "', '" + currentRetailer.getDealerCode() + "', '" + currentRetailer.getDesig() + "'), \n"  );
        }
        //retValue.append(  "('SC00020001', '75', '[Gaia (Sede) / Frotas]'), \n"  );

        retValue.deleteCharAt(retValue.length() -3);
        retValue.append(  ")\n" );
        retValue.append(  "SELECT * FROM (\n" );
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\"," );

        String oldType = "";
        StringBuffer cTemp = new StringBuffer();

        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    retValue.append(  "\"Total Contratos " + oldType + "\", " );
                    cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN CONTRACTS ELSE NULL END) AS \"Total Contratos " + oldType + "\",\n" );
                    oldType =  model[0] ;
                }
            }
            retValue.append(  "\"" + model[1] + "\", " );
            cTemp.append(  "    SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN CONTRACTS ELSE NULL END) AS \"" + model[1] + "\",\n" );
            oldType =  model[0] ;
        }

        retValue.append(  "\"Total Contratos " + oldType + "\", " );
        cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN CONTRACTS ELSE NULL END) AS \"Total Contratos " + oldType + "\",\n" );

        retValue.append(  "\"Total Contratos1\", " );
        cTemp.append(  "    SUM(CONTRACTS) AS \"Total Contratos1\",\n");

        retValue.append(  "\"Total Vendas\", " );
        cTemp.append(  "    SUM(SALES_VALUE) AS \"Total Vendas\",\n");

        retValue.append(  "\"Dif1\", " );
        cTemp.append(  "    SUM(CONTRACTS - PLATES_VALUE) as \"Dif1\",\n");

        retValue.append(  "\"Total Contratos2\", " );
        cTemp.append(  "    SUM(CONTRACTS) AS \"Total Contratos2\",\n");

        retValue.append(  "\"Total Matrículas\", " );
        cTemp.append(  "    SUM(PLATES_VALUE) AS \"Total Matrículas\",\n");

        retValue.append(  "\"Dif2\"\n" );
        cTemp.append(  "    SUM(SALES_VALUE - PLATES_VALUE) as \"Dif2\"\n");

        retValue.append(  "FROM DEALERS LEFT OUTER JOIN\n" );
        retValue.append(  "(SELECT\n" );
        retValue.append(  "    PVM.OID_DEALER,\n" );
        retValue.append(  cTemp );

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE\n");
        retValue.append(  "WHERE\n");
        retValue.append(  "    PVM.AVAILABLE = 1 AND\n");
        retValue.append(  "    PVM.YEAR = " + year + " AND\n");
        retValue.append(  "    PVM.MONTH = " + month + " AND\n");
        retValue.append(  "    PVM.ID = DT.ID_PVM_MONTHLYREPORT AND\n");
        //retValue.append(  "    CM.ACTIVE = 'S' AND\n");
        retValue.append(  "    DT.ID_PVM_CARMODEL = CM.ID AND\n");
        retValue.append(  "    PVM.OID_DEALER = DE.OID_DEALER\n");
        retValue.append(  "    GROUP BY PVM.OID_DEALER) DETAIL ON\n");
        retValue.append(  "    DEALERS.OID_DEALER = DETAIL.OID_DEALER\n");
        retValue.append(  ") GRID ORDER BY ORDINAL, \"Concessionário\"");

        return retValue.toString();
    }


    public static String getToyotaFrotasContractsSql(Vector<String[]> vecRetailerDealers, Vector<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "WITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS  \n"  );
        retValue.append(  "(VALUES\n" );

        Iterator<String[]> iterVec = vecRetailerDealers.iterator();
        //while (iterVec.hasNext()) {
        //	String[] oidDealer_Dealer = (String[]) iterVec.next();
        //	retValue.append(  "('" + oidDealer_Dealer[0] + "', '" + oidDealer_Dealer[2] + "', '" + oidDealer_Dealer[1] + "'), \n"  );
        //}
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );

        retValue.deleteCharAt(retValue.length() -3);
        retValue.append(  ")\n" );
        retValue.append(  "SELECT * FROM (\n" );
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\"," );

        String oldType = "";
        StringBuffer cTemp = new StringBuffer();

        iterVec = vecPVMCarModel.iterator();
        while (iterVec.hasNext()) {
            String[] model = iterVec.next();

            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    retValue.append(  "\"Total Contratos " + oldType + "\", " );
                    cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN CONTRACTS ELSE NULL END) AS \"Total Contratos " + oldType + "\",\n" );
                    oldType =  model[0] ;
                }
            }
            retValue.append(  "\"" + model[1] + "\", " );
            cTemp.append(  "    SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN CONTRACTS ELSE NULL END) AS \"" + model[1] + "\",\n" );
            oldType =  model[0] ;
        }

        retValue.append(  "\"Total Contratos " + oldType + "\", " );
        cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN CONTRACTS ELSE NULL END) AS \"Total Contratos " + oldType + "\",\n" );

        retValue.append(  "\"Total Contratos1\", " );
        cTemp.append(  "    SUM(CONTRACTS) AS \"Total Contratos1\",\n");

        retValue.append(  "\"Total Vendas\", " );
        cTemp.append(  "    SUM(SALES_VALUE) AS \"Total Vendas\",\n");

        retValue.append(  "\"Dif1\", " );
        cTemp.append(  "    SUM(CONTRACTS - PLATES_VALUE) as \"Dif1\",\n");

        retValue.append(  "\"Total Contratos2\", " );
        cTemp.append(  "    SUM(CONTRACTS) AS \"Total Contratos2\",\n");

        retValue.append(  "\"Total Matrículas\", " );
        cTemp.append(  "    SUM(PLATES_VALUE) AS \"Total Matrículas\",\n");

        retValue.append(  "\"Dif2\"\n" );
        cTemp.append(  "    SUM(SALES_VALUE - PLATES_VALUE) as \"Dif2\"\n");

        retValue.append(  "FROM DEALERS LEFT OUTER JOIN\n" );
        retValue.append(  "(SELECT\n" );
        retValue.append(  "    PVM.OID_DEALER,\n" );
        retValue.append(  cTemp );

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE\n");
        retValue.append(  "WHERE\n");
        retValue.append(  "    PVM.AVAILABLE = 1 AND\n");
        retValue.append(  "    PVM.YEAR = " + year + " AND\n");
        retValue.append(  "    PVM.MONTH = " + month + " AND\n");
        retValue.append(  "    PVM.ID = DT.ID_PVM_MONTHLYREPORT AND\n");
        //retValue.append(  "    CM.ACTIVE = 'S' AND\n");
        retValue.append(  "    DT.ID_PVM_CARMODEL = CM.ID AND\n");
        retValue.append(  "    PVM.OID_DEALER = DE.OID_DEALER\n");
        retValue.append(  "    GROUP BY PVM.OID_DEALER) DETAIL ON\n");
        retValue.append(  "    DEALERS.OID_DEALER = DETAIL.OID_DEALER\n");
        retValue.append(  ") GRID ORDER BY ORDINAL, \"Concessionário\"");

        return retValue.toString();
    }

    public static String getLexusFrotasContractsSql(List<RetailDealerDTO> vecRetailerDealers, List<String[]> vecPVMCarModel, String year, String month) {

        StringBuffer retValue = new StringBuffer();
        retValue.append(  "\nWITH DEALERS (OID_DEALER, DEALERCODE, DEALER, SUB_DEALER) AS \n"  );
        retValue.append(  "(VALUES\n" );

        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_SUL+"', '[TCAP Frotas Sul]', "+PVMUtil.SUB_DLR_CODE_FROTAS_SUL+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_IMOBILIZADO+"', '[TCAP Imobilizado (Gaia)]', "+PVMUtil.SUB_DLR_CODE_IMOBILIZADO+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_FROTAS_GAIA+"', '[TCAP Frotas Gaia]', "+PVMUtil.SUB_DLR_CODE_FROTAS_GAIA+"), \n"  );
        retValue.append(  "('"+Dealer.OID_NMSC+"', '"+PVMUtil.DLR_CODE_EXPORTACAO+"', '[TCAP Exportação (Ovar)]', "+PVMUtil.SUB_DLR_CODE_EXPORTACAO+"), \n"  );

        retValue.deleteCharAt(retValue.length() -3);
        retValue.append(  ")\n" );
        retValue.append(  "SELECT * FROM (\n" );
        retValue.append(  "SELECT '1' as ORDINAL, DEALERS.DEALERCODE AS \"Cód Concessionário\", DEALERS.DEALER AS \"Concessionário\"," );

        String oldType = "";
        StringBuffer cTemp = new StringBuffer();

        for (String[] model: vecPVMCarModel) {
            if( ! oldType.equalsIgnoreCase("")) {
                if( ! model[0].equalsIgnoreCase(oldType))  {
                    retValue.append(  "\"Total Contratos " + oldType + "\", " );
                    cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN CONTRACTS ELSE NULL END) AS \"Total Contratos " + oldType + "\",\n" );
                    oldType =  model[0] ;
                }
            }
            retValue.append(  "\"" + model[1] + "\", " );
            cTemp.append(  "    SUM(CASE WHEN ID_PVM_CARMODEL = " + model[2] + " THEN CONTRACTS ELSE NULL END) AS \"" + model[1] + "\",\n" );
            oldType =  model[0] ;
        }


        retValue.append(  "\"Total Contratos " + oldType + "\", " );
        cTemp.append(  "    SUM(CASE WHEN CM.TYPE = '" + oldType + "' THEN CONTRACTS ELSE NULL END) AS \"Total Contratos " + oldType + "\",\n" );

        retValue.append(  "\"Total Contratos1\", " );
        cTemp.append(  "    SUM(CONTRACTS) AS \"Total Contratos1\",\n");

        retValue.append(  "\"Total Vendas\", " );
        cTemp.append(  "    SUM(SALES_VALUE) AS \"Total Vendas\",\n");

        retValue.append(  "\"Dif1\", " );
        cTemp.append(  "    SUM(CONTRACTS - PLATES_VALUE) as \"Dif1\",\n");

        retValue.append(  "\"Total Contratos2\", " );
        cTemp.append(  "    SUM(CONTRACTS) AS \"Total Contratos2\",\n");

        retValue.append(  "\"Total Matrículas\", " );
        cTemp.append(  "    SUM(PLATES_VALUE) AS \"Total Matrículas\",\n");

        retValue.append(  "\"Dif2\"\n" );
        cTemp.append(  "    SUM(SALES_VALUE - PLATES_VALUE) as \"Dif2\"\n");

        retValue.append(  "FROM DEALERS LEFT OUTER JOIN\n" );
        retValue.append(  "(SELECT\n" );
        retValue.append(  "    PVM.OID_DEALER,\n" );
        retValue.append(  cTemp );

        retValue.append(  "FROM PVM_MONTHLYREPORT AS PVM, PVM_MONTHLYREPORTDETAIL AS DT, PVM_CARMODEL AS CM, DEALERS AS DE\n");
        retValue.append(  "WHERE\n");
        retValue.append(  "    PVM.AVAILABLE = 1 AND\n");
        retValue.append(  "    PVM.YEAR = " + year + " AND\n");
        retValue.append(  "    PVM.MONTH = " + month + " AND\n");
        retValue.append(  "    PVM.ID = DT.ID_PVM_MONTHLYREPORT AND\n");
        retValue.append(  "    DT.ID_PVM_CARMODEL = CM.ID AND\n");
        retValue.append(  "    PVM.OID_DEALER = DE.OID_DEALER AND\n");
        retValue.append(  "    PVM.SUB_DEALER = DE.SUB_DEALER \n");
        retValue.append(  "    GROUP BY PVM.OID_DEALER, PVM.SUB_DEALER) DETAIL ON\n");
        retValue.append(  "    DEALERS.OID_DEALER = DETAIL.OID_DEALER\n");
        retValue.append(  ") GRID ORDER BY ORDINAL, \"Concessionário\"");

        return retValue.toString();
    }
}
