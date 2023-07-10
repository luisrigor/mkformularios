package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.dto.PVMBudgetDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PVMBudgetRepositoryCustom {

    List<PVMBudgetDTO> getPVMBudgetByYear(@Param("year") Integer year) ;
}
