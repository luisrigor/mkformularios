package com.gsc.mkformularios.dto;


import com.gsc.mkformularios.model.toyota.entity.PVMCarModelYearForecast;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoToModelDTO {

    private List<PVMCarmodel> car;
    private List<PVMCarModelYearForecast>  forecast;
    private PVMCarmodel carModel;
}
