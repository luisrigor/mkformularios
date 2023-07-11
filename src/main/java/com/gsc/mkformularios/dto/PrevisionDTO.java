package com.gsc.mkformularios.dto;


import com.gsc.mkformularios.model.toyota.entity.UsedCarsPrevisionSales;
import com.rg.dealer.Dealer;
import lombok.*;
import java.util.List;
import java.util.Vector;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrevisionDTO {

    private List<UsedCarsPrevisionSales> hstMonthPrevisionConc;
    private List<UsedCarsPrevisionSales> hstMonthPrevisionTcap;
    private Vector<Dealer> dealers;

}

