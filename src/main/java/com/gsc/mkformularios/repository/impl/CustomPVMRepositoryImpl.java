package com.gsc.mkformularios.repository.impl;

import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.repository.CustomPVMRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CustomPVMRepositoryImpl implements CustomPVMRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<SalesPlates> getSalesAndPlates(int idPVM) {
        String sql = this.buildPVMQuery();
        Query query = em.createNativeQuery(sql, "SalesPlatesMapping");
        List<SalesPlates> salesAndPlates = query.setParameter("idPVMMonthlyReport", idPVM).getResultList();
        return salesAndPlates;
    }

    private String buildPVMQuery() {
        StringBuilder subSql = new StringBuilder();
        StringBuilder subSql1 = new StringBuilder();

        subSql.append("        FROM PVM_MONTHLYREPORT A, PVM_MONTHLYREPORTDETAIL B");
        subSql.append("            WHERE");
        subSql.append("                A.ID = B.ID_PVM_MONTHLYREPORT");
        subSql.append("                AND A.YEAR = YEAR(DATE(RTRIM(LTRIM(CHAR(PVM_MR.YEAR))) || '-' || RTRIM(LTRIM(CHAR(PVM_MR.MONTH))) || '-01') - 1 MONTH )");
        subSql.append("                AND A.MONTH = MONTH(DATE(RTRIM(LTRIM(CHAR(PVM_MR.YEAR))) || '-' || RTRIM(LTRIM(CHAR(PVM_MR.MONTH))) || '-01') - 1 MONTH)");
        subSql.append("                AND B.ID_PVM_CARMODEL = PVM_MRD.ID_PVM_CARMODEL");
        subSql.append("                AND A.OID_DEALER = PVM_MR.OID_DEALER");
        subSql.append("                AND A.SUB_DEALER = PVM_MR.SUB_DEALER");

        subSql1.append("        FROM PVM_MONTHLYREPORT A, PVM_MONTHLYREPORTDETAIL B");
        subSql1.append("            WHERE");
        subSql1.append("                A.ID = B.ID_PVM_MONTHLYREPORT");
        subSql1.append("                AND A.YEAR = YEAR(DATE(RTRIM(LTRIM(CHAR(PVM_MR.YEAR))) || '-' || RTRIM(LTRIM(CHAR(PVM_MR.MONTH))) || '-01') - 2 MONTH )");
        subSql1.append("                AND A.MONTH = MONTH(DATE(RTRIM(LTRIM(CHAR(PVM_MR.YEAR))) || '-' || RTRIM(LTRIM(CHAR(PVM_MR.MONTH))) || '-01') - 2 MONTH)");
        subSql1.append("                AND B.ID_PVM_CARMODEL = PVM_MRD.ID_PVM_CARMODEL");
        subSql1.append("                AND A.OID_DEALER = PVM_MR.OID_DEALER");
        subSql1.append("                AND A.SUB_DEALER = PVM_MR.SUB_DEALER");

        String sql = "WITH DETAIL AS (" +
                "    SELECT PVM_MRD.ID_PVM_MONTHLYREPORT, PVM_MR.YEAR, PVM_MR.MONTH, PVM_MR.OID_DEALER, PVM_MR.SUB_DEALER, PVM_MR.REASON, PVM_MR.AVAILABLE, PVM_MRD.SALES_VALUE, PVM_MRD.CONTRACTS," +
                "    (SELECT B.SALES_VALUE2" +
                subSql +
                "    ) AS SALES_VALUEP1," +
                "    (SELECT B.SALES_VALUE3" +
                subSql1 +
                "    ) AS SALES_VALUEP2," +
                "    PVM_MRD.PLATES_VALUE," +
                "    (SELECT B.PLATES_VALUE2" +
                subSql +
                "    ) AS PLATES_VALUEP1," +
                "    (SELECT B.PLATES_VALUE3" +
                subSql1 +
                "    ) AS PLATES_VALUEP2," +
                "    PVM_MRD.SALES_VALUE2," +
                "    (SELECT B.SALES_VALUE3" +
                subSql +
                "    ) AS SALES_VALUEP3," +
                "    PVM_MRD.PLATES_VALUE2," +
                "    (SELECT B.PLATES_VALUE3" +
                subSql +
                "            ) AS PLATES_VALUEP3," +
                "            PVM_MRD.SALES_VALUE3," +
                "            PVM_MRD.PLATES_VALUE3," +
                " PVM_MRD.VDVC," +
                " PVM_MRD.VDVC2," +
                " PVM_MRD.VDVC3," +
                "    (SELECT B.VDVC2" +
                subSql +
                "    ) AS VDVCP1, " +
                "    (SELECT B.VDVC3" +
                subSql1 +
                "    ) AS VDVCP2, " +
                "    (SELECT B.VDVC3" +
                subSql +
                "    ) AS VDVCP3," +
                "            PVM_CM.ID AS BRAND_ID," +
                "            DATE(RTRIM(LTRIM(CHAR(PVM_MR.YEAR))) || '-' || RTRIM(LTRIM(CHAR(PVM_MR.MONTH))) || '-01') AS DT_SELECT, PVM_CM.DT_FROM, PVM_CM.DT_TO" +
                "                FROM PVM_MONTHLYREPORT PVM_MR, PVM_MONTHLYREPORTDETAIL PVM_MRD, PVM_CARMODEL PVM_CM" +
                "                    WHERE PVM_MR.ID = PVM_MRD.ID_PVM_MONTHLYREPORT AND PVM_MRD.ID_PVM_CARMODEL = PVM_CM.ID" +
                "                        AND ID_PVM_MONTHLYREPORT = :idPVMMonthlyReport " +
                "    )" +
                "    SELECT ID_PVM_MONTHLYREPORT, DETAIL.YEAR, DETAIL.MONTH, DETAIL.OID_DEALER, BRAND_ID, DT_SELECT, DT_FROM, DT_TO," +
                "    SALES_VALUEP2,SALES_VALUEP1, SALES_VALUE," +
                "    SALES_VALUEP3,SALES_VALUE2," +
                "    SALES_VALUE3," +
                "    PLATES_VALUEP2,PLATES_VALUEP1,PLATES_VALUE," +
                "    PLATES_VALUEP3,PLATES_VALUE2," +
                "    PLATES_VALUE3, " +
                "    CONTRACTS," +
                "    B.PLATES AS BUDGET," +
                "    VDVCP2,VDVCP1,VDVC," +
                "    VDVCP3,VDVC2," +
                "    VDVC3 " +
                "    FROM DETAIL" +
                "    LEFT JOIN PVM_BUDGET B ON DETAIL.OID_DEALER = B.OID_DEALER AND DETAIL.SUB_DEALER = B.SUB_DEALER AND DETAIL.YEAR = B.YEAR AND DETAIL.MONTH = B.MONTH AND DETAIL.BRAND_ID = B.ID_PVM_CARMODEL" +
                "    ORDER BY DETAIL.YEAR, DETAIL.MONTH, DETAIL.OID_DEALER";

        return sql;
    }

}
