package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetDTO {
   List<BudgetDealerDTO> budgetDealers;
    int year;
    List<MapTypesDTO> carTypes;
}
