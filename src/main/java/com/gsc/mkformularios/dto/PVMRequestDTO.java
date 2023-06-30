package com.gsc.mkformularios.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PVMRequestDTO {

    private int idPVM;
    private int year;
    private int month;
    private String cancelReasons;

    public String getWhereClause() {
        StringBuilder filter = new StringBuilder();
        filter.append(" AND YEAR = :yearRe ");

        if (this.month > 0 )
            filter.append(" AND MONTH =  :monthRe");

        return filter.toString();
    }
}
