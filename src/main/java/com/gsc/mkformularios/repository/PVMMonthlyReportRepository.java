package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PVMMonthlyReportRepository extends JpaRepository<PVMMonthlyReport, Integer>, CustomPVMRepository {
}
