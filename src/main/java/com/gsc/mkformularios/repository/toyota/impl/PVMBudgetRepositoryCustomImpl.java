package com.gsc.mkformularios.repository.toyota.impl;

import com.gsc.mkformularios.dto.PVMBudgetDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepositoryCustom;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class PVMBudgetRepositoryCustomImpl implements PVMBudgetRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<PVMBudgetDTO> getPVMBudgetByYear(Integer year) {
        String sql = "SELECT PB.* FROM PVM_BUDGET PB WHERE PB.year = :year ORDER BY PB.OID_DEALER, PB.month, PB.ID_PVM_CARMODEL";
        Query query = em.createNativeQuery(sql, "GetBudgetYear");
        List<PVMBudgetDTO> pvmBudgetList = query.setParameter("year", year)
                .getResultList();
        return pvmBudgetList;
    }
}
