package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.model.toyota.entity.UsedCarsPrevisionSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrevisionRepository extends JpaRepository<UsedCarsPrevisionSales, Integer> {

    @Query(value = "SELECT OID_DEALER, SUM(PREVISION_TVC) AS PREVISION_TVC, SUM(PREVISION_SN) AS PREVISION_SN FROM TVC_USED_CARS_PREVISION_SALES WHERE YEAR= :year AND MONTH <= :month AND PREVISION_TYPE= :previsionType  AND STATUS='Fechado' GROUP BY OID_DEALER" , nativeQuery = true)
    List<UsedCarsPrevisionSales> previsionAnnual(@Param("year") Integer year,@Param("month") Integer month, @Param("reportType") String reportType, @Param("previsionType") String previsionType);
    @Query(value = "SELECT * FROM TVC_USED_CARS_PREVISION_SALES WHERE YEAR= :year AND MONTH= :month AND PREVISION_TYPE= :previsionType",nativeQuery = true)
    List<UsedCarsPrevisionSales> previsionMonthly(@Param("year") Integer year,@Param("month") Integer month, @Param("reportType") String reportType, @Param("previsionType") String previsionType);
    @Query(value = " SELECT * FROM TVC_USED_CARS_PREVISION_SALES WHERE YEAR= :year AND MONTH= :month AND PREVISION_TYPE= :previsionType AND STATUS='Fechado'",nativeQuery = true)
    List<UsedCarsPrevisionSales> previsionExcell(@Param("year") Integer year,@Param("month") Integer month, @Param("reportType") String reportType, @Param("previsionType") String previsionType);
}
