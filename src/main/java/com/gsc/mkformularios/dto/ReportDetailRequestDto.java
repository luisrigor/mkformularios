package com.gsc.mkformularios.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDetailRequestDto {


    private int idModel;
    private String contract;
    private String s1;
    private String s2;
    private String s3;
    private String p1;
    private String p2;
    private String p3;
    private String v1;
    private String v2;
    private String v3;

}
