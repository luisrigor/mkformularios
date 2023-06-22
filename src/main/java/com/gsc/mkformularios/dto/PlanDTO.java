package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.model.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.entity.PVMCarmodel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanDTO {

    private List<PVMCarmodel> car;
    List<PVMCarModelYearForecast>  forecast;

}
