package com.gsc.mkformularios.model.toyota.entity;

import lombok.*;

import javax.persistence.Entity;
import java.util.Hashtable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrevisionFilterBean {

        private String ivOidNet;
        private String ivOidDealer;
        private int ivYear;
        private int ivMonth;
        private String ivOrderColumn;
        private String ivOrderOrientation;
        private boolean ivValidOpenMonth;
        private Hashtable<Integer, String> ivHstMonths;
        private int ivActualYear;
        private int ivActualMonth;
}
