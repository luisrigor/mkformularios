package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PVMCarModelYearForecastRepository extends JpaRepository<PVMCarModelYearForecast, Integer> {
    List<PVMCarModelYearForecast> findAllByYear(int year);

    List<PVMCarModelYearForecast> findPVMCarModelYearForecastById(Integer id);

    @Modifying
    @Query("DELETE FROM PVMCarModelYearForecast WHERE idCarModel = :idCarModel AND year = :year ")
    void deleteCarModelByIdAndYear(@Param("idCarModel") Integer idCarModel, @Param("year") Integer year);


}
