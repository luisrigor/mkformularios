package com.gsc.mkformularios.repository.toynet;

import java.util.List;

public interface CustomRetailerRepository {

    List<String> getNotSendPVM(String tableName, List<String> oidDealers);
}
