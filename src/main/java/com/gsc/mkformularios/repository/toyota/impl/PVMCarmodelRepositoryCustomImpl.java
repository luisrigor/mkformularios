package com.gsc.mkformularios.repository.toyota.impl;

import com.gsc.mkformularios.dto.MapTypesDTO;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepositoryCustom;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class PVMCarmodelRepositoryCustomImpl implements PVMCarmodelRepositoryCustom {


    @PersistenceContext
    private EntityManager em;


    @Override
    @Transactional
    public List<MapTypesDTO> getCarTypes() {
        StringBuilder sql = new StringBuilder(
                "SELECT MIN(PC.ID) AS ID, PC.TYPE  FROM PVM_CARMODEL PC " +
                "WHERE PC.active LIKE 'S' " +
                "AND CURRENT DATE BETWEEN PC.DT_FROM AND VALUE( PC.DT_TO, '9999-12-31') " +
                " GROUP BY TYPE ORDER BY ID") ;

        Query query = em.createNativeQuery(sql.toString(), "GetCarTypesMapping");

        List<MapTypesDTO> typesDTO = query.getResultList();
        return typesDTO;
    }
}
