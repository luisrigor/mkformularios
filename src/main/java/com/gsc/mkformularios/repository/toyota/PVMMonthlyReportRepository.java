package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PVMMonthlyReportRepository extends JpaRepository<PVMMonthlyReport, Integer>, CustomPVMRepository {

    @Query(value = "SELECT * FROM PVM_MONTHLYREPORT WHERE 1=1 AND YEAR = :year AND MONTH = :month AND OID_DEALER = :oidDealer AND SUB_DEALER = :subDealer", nativeQuery = true)
    Optional<PVMMonthlyReport> newPVM(@Param("year") int year, @Param("month") int month, @Param("oidDealer") String oidDealer, @Param("subDealer") int subDealer);


    @Query("SELECT PMR.oidDealer FROM PVMMonthlyReport  PMR WHERE PMR.year = :year AND PMR.month = :month AND PMR.available = 1")
    List<String> getNotSendPVMOid(@Param("year") int year, @Param("month") int month);


}
