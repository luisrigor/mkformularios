package com.gsc.mkformularios.repository.toyota.impl;

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
        String sql = this.buildPVMSalesQuery();
        Query query = em.createNativeQuery(sql, "SalesPlatesMapping");
        List<SalesPlates> salesAndPlates = query.setParameter("idPVMMonthlyReport", idPVM).getResultList();
        return salesAndPlates;
    }

    @Override
    public List<PVMMonthlyReport> getPVM() {
//        String sql = buildPVMQuery(whereClause, roles, oidDealerParent, oidNet);
//        Query query = em.createNativeQuery(sql, PVMMonthlyReport.class);
//        List<PVMMonthlyReport> pvmMonthlyReports = query.getResultList();
        return null;
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


    private String buildPVMSalesQuery() {
        StringBuilder sql = new StringBuilder();
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

        sql.append("WITH DETAIL AS (");
        sql.append("    SELECT PVM_MRD.ID_PVM_MONTHLYREPORT, PVM_MR.YEAR, PVM_MR.MONTH, PVM_MR.OID_DEALER, PVM_MR.SUB_DEALER, PVM_MR.REASON, PVM_MR.AVAILABLE, PVM_MRD.SALES_VALUE, PVM_MRD.CONTRACTS,");
        sql.append("    (SELECT B.SALES_VALUE2");
        sql.append(subSql);
        sql.append("    ) AS SALES_VALUEP1,");
        sql.append("    (SELECT B.SALES_VALUE3");
        sql.append(subSql1);
        sql.append("    ) AS SALES_VALUEP2,");
        sql.append("    PVM_MRD.PLATES_VALUE,");
        sql.append("    (SELECT B.PLATES_VALUE2");
        sql.append(subSql);
        sql.append("    ) AS PLATES_VALUEP1,");
        sql.append("    (SELECT B.PLATES_VALUE3");
        sql.append(subSql1);
        sql.append("    ) AS PLATES_VALUEP2,");
        sql.append("    PVM_MRD.SALES_VALUE2,");
        sql.append("    (SELECT B.SALES_VALUE3");
        sql.append(subSql);
        sql.append("    ) AS SALES_VALUEP3,");
        sql.append("    PVM_MRD.PLATES_VALUE2,");
        sql.append("    (SELECT B.PLATES_VALUE3");
        sql.append(subSql);
        sql.append("            ) AS PLATES_VALUEP3,");
        sql.append("            PVM_MRD.SALES_VALUE3,");
        sql.append("            PVM_MRD.PLATES_VALUE3,");
        sql.append(" PVM_MRD.VDVC,");
        sql.append(" PVM_MRD.VDVC2,");
        sql.append(" PVM_MRD.VDVC3,");
        sql.append("    (SELECT B.VDVC2");
        sql.append(subSql);
        sql.append("    ) AS VDVCP1, ");
        sql.append("    (SELECT B.VDVC3");
        sql.append(subSql1);
        sql.append("    ) AS VDVCP2, ");
        sql.append("    (SELECT B.VDVC3");
        sql.append(subSql);
        sql.append("    ) AS VDVCP3,");
        sql.append("            PVM_CM.ID AS BRAND_ID,");
        sql.append("            DATE(RTRIM(LTRIM(CHAR(PVM_MR.YEAR))) || '-' || RTRIM(LTRIM(CHAR(PVM_MR.MONTH))) || '-01') AS DT_SELECT, PVM_CM.DT_FROM, PVM_CM.DT_TO");
        sql.append("                FROM PVM_MONTHLYREPORT PVM_MR, PVM_MONTHLYREPORTDETAIL PVM_MRD, PVM_CARMODEL PVM_CM");
        sql.append("                    WHERE PVM_MR.ID = PVM_MRD.ID_PVM_MONTHLYREPORT AND PVM_MRD.ID_PVM_CARMODEL = PVM_CM.ID");
        sql.append("                        AND ID_PVM_MONTHLYREPORT = :idPVMMonthlyReport ");
        sql.append("    )");
        sql.append("    SELECT ID_PVM_MONTHLYREPORT, DETAIL.YEAR, DETAIL.MONTH, DETAIL.OID_DEALER, BRAND_ID, DT_SELECT, DT_FROM, DT_TO,");
        sql.append("    SALES_VALUEP2,SALES_VALUEP1, SALES_VALUE,");
        sql.append("    SALES_VALUEP3,SALES_VALUE2,");
        sql.append("    SALES_VALUE3,");
        sql.append("    PLATES_VALUEP2,PLATES_VALUEP1,PLATES_VALUE,");
        sql.append("    PLATES_VALUEP3,PLATES_VALUE2,");
        sql.append("    PLATES_VALUE3, ");
        sql.append("    CONTRACTS,");
        sql.append("    B.PLATES AS BUDGET,");
        sql.append("    VDVCP2,VDVCP1,VDVC,");
        sql.append("    VDVCP3,VDVC2,");
        sql.append("    VDVC3 ");
        sql.append("    FROM DETAIL");
        sql.append("    LEFT JOIN PVM_BUDGET B ON DETAIL.OID_DEALER = B.OID_DEALER AND DETAIL.SUB_DEALER = B.SUB_DEALER AND DETAIL.YEAR = B.YEAR AND DETAIL.MONTH = B.MONTH AND DETAIL.BRAND_ID = B.ID_PVM_CARMODEL");
        sql.append("    ORDER BY DETAIL.YEAR, DETAIL.MONTH, DETAIL.OID_DEALER");

        return sql.toString();
    }


    private String buildPVMQuery(String whereClause, List<String> roles, String oidDealerParent, String oidNet) throws SCErrorException {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM PVM_MONTHLYREPORT ");
        sql.append(" WHERE 1=1 ");

        if(whereClause.trim().length() > 0) {
            sql.append(whereClause+" ");
        }
        if (roles.contains("DEALER")) {
            sql.append(" AND OID_DEALER ='"  +oidDealerParent+ "' ORDER BY MONTH");
        } else if (roles.contains("TCAP")) {
            sql.append(" AND (AVAILABLE = 1 OR OID_DEALER ='" +oidDealerParent+ "')");
        } else if (roles.contains("IS_CA_MANAGER")) {
            sql.append(" AND OID_DEALER IN ("+Dealer.getHelper().getActiveOidsDealersForParent(oidNet, Dealer.OID_CAETANO_AUTO)+")");
        } else {
            sql.append(" AND 1=2 ");
        }

        return sql.toString();
    }

    private List<Map<String,Object>> orderedMap(List<Tuple> result) {
        List<Map<String, Object>> resultMap = new ArrayList<>();

        for (Tuple tuple : result) {
            Map<String, Object> rowMap = new HashMap<>();
            List<TupleElement<?>> columnNames = tuple.getElements();

            for (TupleElement<?> v: columnNames) {

//                Object columnValue = tuple.get(columnName);
//                rowMap.put(columnName, columnValue);
            }

            resultMap.add(rowMap);
        }

        return resultMap;
    }

}
