package com.gsc.mkformularios.model.toynet.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "LEXUS_RETAILER")
public class LexusRetailer {
        @Id
        private String objectId;
        private String status;
        private String isCaMember;
        private String isGscMember;
        private String hasSinage;
        private String hasNcr;
        private Float infoTotalArea;
        private Float infoShowroom;
        private Float infoAfterSales;
        private String toyotaDealerCode;
        private String facilityCode;
        private String dealerContract;
        private String oidInstallationType;
        private Integer cp4;
        private Integer cp3;
        private LocalDate dtOpenInstallation;
        private LocalDate dtCloseInstallation;
        private LocalDate sinageDtImplemented;
        private LocalDate ncrDtImplemented;
        private String tel;
        private String fax;
        private String salesCode;
        private String aftersalesCode;
        private String dealerCode;
        private String nif;
        private String oidParent;
        private String nrDoor;
        private String oidNet;
        private String tarsUoid;
        private String oidBrand;
        private String oidTrader;
        private String floor;
        private String gpsX;
        private String gpsY;
        private LocalDateTime dtCreated;
        private LocalDateTime dtChanged;
        private String suffixLogin;
        private String resp;
        private String cpExt;
        private String municipality;
        private String district;
        private String country;
        private String logo;
        private String map;
        private String image;
        private String desig;
        private String comercialName;
        private String urlMinisiteLexus;
        private String urlExterno;
        private String urlUsados;
        private String idMinisite;
        private String createdBy;
        private String changedBy;
        private String urlDmsWs;
        private String extManagingCompanyId;
        private String extHoldingId;
        private String obs;
        private String email;
        private String end;
}
