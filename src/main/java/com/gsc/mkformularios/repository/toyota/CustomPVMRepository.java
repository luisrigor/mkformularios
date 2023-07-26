package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.dto.PVMCarmodelForecast;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CustomPVMRepository {

    List<SalesPlates> getSalesAndPlates(int idPVM);
    List<PVMMonthlyReport> getPVM();
    List<Map<String,Object>> getPVMMonthReportPlates(String platesSql);
    List<PVMCarmodelForecast> getPVMCarModelsForecasts(String year, String month, String dtFrom);
}
