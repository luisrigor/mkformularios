package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.model.entity.PVMCarmodel;
import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PVMDetailDTO {

    private List<PVMCarmodel> car;
    private PVMMonthlyReport monthlyReport;
    private List<SalesPlates> salesAndPlates;
}
