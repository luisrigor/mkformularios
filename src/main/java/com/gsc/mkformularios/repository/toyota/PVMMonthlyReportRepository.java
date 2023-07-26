package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PVMMonthlyReportRepository extends JpaRepository<PVMMonthlyReport, Integer>, CustomPVMRepository {
}
