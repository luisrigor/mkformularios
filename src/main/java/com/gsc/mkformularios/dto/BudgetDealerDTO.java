package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BudgetDealerDTO {

    String[] dealers;
    List<PVMBudgetDTO> budgets;

}
