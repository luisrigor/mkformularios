package com.gsc.mkformularios.model.toynet.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TOYOTA_RETAILER")
public class ToyotaRetailer {

        @Id
        private String objectid;
        private String desig;
        @Column(name = "OID_PARENT")
        private String oidParent;
        @Column(name = "OID_INSTALLATION_TYPE")
        private String oidInstallationType;
        private String end;
        private String tel;
        private String resp;
        private String obs;
        private String email;
        private Integer cp4;
        private Integer cp3;
        private String cpext;
        private String fax;
        @Column(name = "SUFFIX_LOGIN")
        private String suffixLogin;
        @Column(name = "COMERCIAL_NAME")
        private String comercialName;
        private String municipality;
        private String district;
        private String country;
        @Column(name = "URL_MINISITE_TOYOTA")
        private String urlMinisiteToyota;
        @Column(name = "URL_EXTERNO")
        private String urlExterno;
        @Column(name = "URL_USADOS")
        private String urlUsados;
        @Column(name = "SALES_CODE")
        private String salesCode;
        @Column(name = "AFTERSALES_CODE")
        private String aftersalesCode;
        @Column(name = "ID_MINISITE")
        private String idMinisite;
        private String logo;
        private String map;
        private String image;
        @Column(name = "DEALER_CODE")
        private String dealerCode;
        @Column(name = "TOYOTA_DEALER_CODE")
        private String toyotaDealerCode;
        @Column(name = "FACILITY_CODE")
        private String facilityCode;
        private char status;
        private String nrdoor;
        private String floor;
        @Column(name = "OID_NET")
        private String oidNet;
        @Column(name = "TARS_UOID")
        private String tarsUoid;
        @Column(name = "IS_CA_MEMBER")
        private char isCaMember;
        @Column(name = "IS_GSC_MEMBER")
        private char isGscMember;
        @Column(name = "CREATED_BY")
        private String createdBy;
        @Column(name = "DT_CREATED")
        private LocalDateTime dtCreated;
        @Column(name = "CHANGED_BY")
        private String changedBy;
        @Column(name = "DT_CHANGED")
        private LocalDateTime dtChanged;
        @Column(name = "DEALER_CONTRACT")
        private String dealerContract;
        @Column(name = "DT_OPEN_INSTALATION")
        private LocalDate dtOpenInstalation;
        @Column(name = "DT_CLOSE_INSTALATION")
        private LocalDate dtCloseInstalation;
        @Column(name = "GPS_X")
        private String gpsX;
        @Column(name = "GPS_Y")
        private String gpsY;
        @Column(name = "HAS_SINAGE")
        private char hasSinage;
        @Column(name = "SINAGE_DT_IMPLEMENTED")
        private LocalDate sinageDtImplemented;
        @Column(name = "HAS_NCR")
        private char hasNcr;
        @Column(name = "NCR_DT_IMPLEMENTED")
        private LocalDate ncrDtImplemented;
        private String nif;
        @Column(name = "URL_DMS_WS")
        private String urlDmsWs;
        @Column(name = "OID_BRAND")
        private String oidBrand;
        @Column(name = "OID_TRADER")
        private String oidTrader;
        @Column(name = "INFO_TOTAL_AREA")
        private Float infoTotalArea;
        @Column(name = "INFO_SHOWROOM")
        private Float infoShowroom;
        @Column(name = "INFO_AFTER_SALES")
        private Float infoAfterSales;
        @Column(name = "EXT_MANAGING_COMPANY_ID")
        private String extManagingCompanyId;
        @Column(name = "EXT_HOLDING_ID")
        private String extHoldingId;
}
