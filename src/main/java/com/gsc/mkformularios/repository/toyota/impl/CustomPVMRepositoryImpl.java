package com.gsc.mkformularios.repository.toyota.impl;

import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toyota.CustomPVMRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;
import com.gsc.mkformularios.config.AliasToEntityMapResultTransformer;
import com.gsc.mkformularios.dto.PVMCarmodelForecast;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toyota.CustomPVMRepository;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import org.hibernate.query.internal.NativeQueryImpl;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.util.*;

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

    @Override
    public List<PVMMonthlyReport> getPVM(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal) {
        try {
            String sql = this.buildGotoPVMQuery(pvmRequestDTO, userPrincipal);
            Query query = em.createNativeQuery(sql, PVMMonthlyReport.class)
                    .setParameter("yearRe", pvmRequestDTO.getYear());
            if (pvmRequestDTO.getMonth()>0)
                query = query
                        .setParameter("monthRe", pvmRequestDTO.getMonth());
            List<PVMMonthlyReport> pvmMonthlyReports = query.getResultList();
            return pvmMonthlyReports;
        } catch (SCErrorException e) {
            throw new RuntimeException("Error fetching PVM ", e);
        }
    }

    public List<Map<String,Object>> getPVMMonthReportPlates(String platesSql) {
        Query query = em.createNativeQuery(platesSql);


        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        List<Map<String,Object>> result = nativeQuery.getResultList();

        return result;
    }

    @Override
    public List<PVMCarmodelForecast> getPVMCarModelsForecasts(String year, String month, String dtFrom) {
        String SQL = " SELECT CM.*, CYF.FORECAST FROM PVM_CARMODEL CM " +
                "LEFT JOIN PVM_CARMODEL_YEAR_FORECASTS CYF ON CM.ID = CYF.ID_CARMODEL " +
                "AND CYF.YEAR = "+year+" AND CYF.MONTH = "+month+" " +
                "WHERE DT_FROM <= date('" + dtFrom + "') AND VALUE(DT_TO, date('9999-12-31')) >= date('" + year + "-" + month + "-01') order by TYPE, EXPORT_ORDER, NAME ";

        Query query = em.createNativeQuery(SQL, "GetCarModelsForecastMapping");

        List<PVMCarmodelForecast> carmodelForecasts = query.getResultList();
        return carmodelForecasts;
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

    private String buildGotoPVMQuery(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal) throws SCErrorException {
        StringBuilder sql = new StringBuilder();
        String whereClause = pvmRequestDTO.getWhereClause();

        sql.append(" SELECT * FROM PVM_MONTHLYREPORT ");
        sql.append(" WHERE 1=1 ");
        if(whereClause.trim().length() > 0) {
            sql.append(whereClause).append(" ");
        }
        Set<AppProfile> roles = userPrincipal.getRoles();

        if (roles.contains(AppProfile.DEALER)) {
            // Mostra dados de determinada concess�o
            sql.append(" AND OID_DEALER ='"  + userPrincipal.getOidDealerParent()+"' ORDER BY MONTH");
        } else if (roles.contains(AppProfile.TCAP)) {
            // Mostra s� os que est�o disponiveis TCAP
            sql.append(" AND (AVAILABLE = 1 OR OID_DEALER ='"+ userPrincipal.getOidDealerParent()+"')");
        } else if (roles.contains(AppProfile.CA)) {
            // Mostra s� os que est�o Caetano Auto
            sql.append(" AND OID_DEALER IN ("+ Dealer.getHelper().getActiveOidsDealersForParent(userPrincipal.getOidNet(), Dealer.OID_CAETANO_AUTO)+")");
        } else {
            sql.append(" AND 1=2 ");
        }

        return sql.toString();
    }
}
