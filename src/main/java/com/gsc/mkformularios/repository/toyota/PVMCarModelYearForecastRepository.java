package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PVMCarModelYearForecastRepository extends JpaRepository<PVMCarModelYearForecast, Integer> {
    List<PVMCarModelYearForecast> findAllByYear(int year);

    List<PVMCarModelYearForecast> findPVMCarModelYearForecastById(Integer id);

}
