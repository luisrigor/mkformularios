package com.gsc.mkformularios.repository.toynet.impl;

import com.gsc.mkformularios.repository.toynet.CustomRetailerRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class CustomRetailerRepositoryImpl implements CustomRetailerRepository {

    @PersistenceContext
    EntityManager em;


    @Override
    public List<String> getNotSendPVM(String tableName, List<String> oidDealersArra) {
        StringBuilder oidDealers = new StringBuilder();
        oidDealers.append("'-1'");
        for (String  currentOid: oidDealersArra) {
            oidDealers.append(",'" +currentOid+ "'");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT OBJECTID FROM " +tableName+ " WHERE OBJECTID NOT IN (" +oidDealers+ ")");

        List<String> resultList = em.createNativeQuery(sql.toString(), String.class).getResultList();
        return  resultList;

    }
}
