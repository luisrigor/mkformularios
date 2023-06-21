package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PVMMonthlyReportRepository extends JpaRepository<PVMMonthlyReport, Integer>, CustomPVMRepository {

    @Query(value = "SELECT * FROM PVM_MONTHLYREPORT WHERE 1=1 AND YEAR = :year AND MONTH = :month AND OID_DEALER = :oidDealer AND SUB_DEALER = :subDealer", nativeQuery = true)
    Optional<PVMMonthlyReport> newPVM(@Param("year") int year, @Param("month") int month, @Param("oid_dealer") String oidDealer, @Param("sub_dealer") int subDealer);


}
