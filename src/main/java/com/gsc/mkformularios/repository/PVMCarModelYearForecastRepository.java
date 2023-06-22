package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.model.entity.PVMCarModelYearForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PVMCarModelYearForecastRepository extends JpaRepository<PVMCarModelYearForecast, Long> {
    List<PVMCarModelYearForecast> findAllYear(int year);

}
