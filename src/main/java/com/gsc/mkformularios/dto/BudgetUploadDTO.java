package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetUploadDTO {
    List<PVMBudget> lstPVMBudget;
    String isNewBudget;
    String yearSelect;



}
