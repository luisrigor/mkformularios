package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PVMBudgetRepository extends JpaRepository<PVMBudget, String> {

    @Query("SELECT PB FROM PVMBudget PB WHERE PB.year = :year ORDER BY PB.oidDealer, PB.month, PB.idPvmCarModel ")
    List<PVMBudget> getPVMBudgetByYear(@Param("year") Integer year) ;
}
