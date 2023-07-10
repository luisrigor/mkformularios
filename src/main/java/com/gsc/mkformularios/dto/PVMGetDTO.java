package com.gsc.mkformularios.dto;

import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.rg.dealer.Dealer;
import lombok.*;

import java.util.Hashtable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PVMGetDTO {

    List<PVMMonthlyReport> pvmMonthlyReports;
    List<DealerDTO> mapDealers;
    List<DealerDTO> notSendPVM;
}
