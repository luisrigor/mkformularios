package com.gsc.mkformularios.dto;

import com.rg.dealer.Dealer;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DealerDTO {

    private String key;
    private Dealer dealer;
}
