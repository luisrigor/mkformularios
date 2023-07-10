package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PVMMonthlyReportDetailRepository extends JpaRepository<PVMMonthlyReportDetail, Integer>, PVMMonthlyReportDetailRepositoryCustom {


    @Modifying
    @Query("UPDATE PVMMonthlyReportDetail PMRD SET PMRD.createdBy =:createdBy, PMRD.dtCreated = :dtCreated, PMRD.salesValue = :salesValue, " +
            "PMRD.platesValue = :platesValue, PMRD.salesValue2 = :salesValue2, PMRD.salesValue3 = :salesValue3, PMRD.platesValue2 = :platesValue2, " +
            "PMRD.platesValue3 = :platesValue3, PMRD.contracts = :contracts, PMRD.vdvc = :vdvc, PMRD.vdvc2 = :vdvc2, PMRD.vdvc3 = :vdvc3 " +
            "WHERE PMRD.monthlyReportId = :monthlyReportId AND PMRD.carModelId = :carModelId " )
    void updateReportDetail(@Param("createdBy") String createdBy,
                            @Param("dtCreated") LocalDateTime dtCreated,
                            @Param("salesValue") Integer salesValue,
                            @Param("platesValue") Integer platesValue,
                            @Param("salesValue2") Integer salesValue2,
                            @Param("salesValue3") Integer salesValue3,
                            @Param("platesValue2") Integer platesValue2,
                            @Param("platesValue3") Integer platesValue3,
                            @Param("contracts") Integer contracts,
                            @Param("vdvc") Integer vdvc,
                            @Param("vdvc2") Integer vdvc2,
                            @Param("vdvc3") Integer vdvc3,
                            @Param("monthlyReportId") Integer monthlyReportId,
                            @Param("carModelId") Integer carModelId);
}
