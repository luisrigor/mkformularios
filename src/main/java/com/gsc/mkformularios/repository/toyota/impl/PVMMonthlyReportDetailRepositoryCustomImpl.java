package com.gsc.mkformularios.repository.toyota.impl;

import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportDetailRepositoryCustom;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PVMMonthlyReportDetailRepositoryCustomImpl implements PVMMonthlyReportDetailRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void mergePVMMonthlyReportDetail(int idPVM) {
        em.createNativeQuery("MERGE INTO PVM_MONTHLYREPORTDETAIL PMR"
                + "USING ( SELECT ("+ idPVM +") AS ID_PVM_MONTHLYREPORT, ID AS ID_PVM_CARMODEL, 0 AS SALES_VALUE, 0 AS PLATES_VALUE, 'System' AS CREATED_BY, CURRENT TIMESTAMP AS DT_CREATED from PVM_CARMODEL"
                + "WHERE dt_from <= current date + 2 months and (dt_to is null or DT_TO >= current date)) NEWLINES"
                + "ON (PMR.ID_PVM_MONTHLYREPORT = NEWLINES.ID_PVM_MONTHLYREPORT AND PMR.ID_PVM_CARMODEL = NEWLINES.ID_PVM_CARMODEL)"
                + "WHEN NOT MATCHED THEN"
                + "INSERT (ID_PVM_MONTHLYREPORT, ID_PVM_CARMODEL, SALES_VALUE, PLATES_VALUE, CREATED_BY, DT_CREATED)"
                + "VALUES(NEWLINES.ID_PVM_MONTHLYREPORT, NEWLINES.ID_PVM_CARMODEL, NEWLINES.SALES_VALUE, NEWLINES.PLATES_VALUE, NEWLINES.CREATED_BY, NEWLINES.DT_CREATED)")
                .setParameter(1, idPVM)
                .executeUpdate();
    }
}



