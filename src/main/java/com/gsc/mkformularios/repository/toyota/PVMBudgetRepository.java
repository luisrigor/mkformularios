package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PVMBudgetRepository extends JpaRepository<PVMBudget, String>, PVMBudgetRepositoryCustom {

    @Modifying
    @Query("DELETE FROM PVMBudget WHERE oidDealer = :oidDealer AND year = :year AND subDealer = :subDealer")
    void deleteBudgetsByDealerAndYear(@Param("oidDealer") String oidDealer, @Param("year") Integer year, @Param("subDealer") Integer subDealer);


}
