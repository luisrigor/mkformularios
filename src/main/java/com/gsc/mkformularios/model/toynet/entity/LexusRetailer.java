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
        @Column(name = "OBJECTID")
        private String objectId;

        @Column(name = "DESIG")
        private String desig;

        @Column(name = "OID_PARENT")
        private String oidParent;

        @Column(name = "OID_INSTALLATION_TYPE")
        private String oidInstallationType;

        @Column(name = "END")
        private String end;

        @Column(name = "TEL")
        private String tel;

        @Column(name = "RESP")
        private String resp;

        @Column(name = "OBS")
        private String obs;

        @Column(name = "EMAIL")
        private String email;

        @Column(name = "CP4")
        private Integer cp4;

        @Column(name = "CP3")
        private Integer cp3;

        @Column(name = "CPEXT")
        private String cpext;

        @Column(name = "FAX")
        private String fax;

        @Column(name = "SUFFIX_LOGIN")
        private String suffixLogin;

        @Column(name = "COMERCIAL_NAME")
        private String comercialName;

        @Column(name = "MUNICIPALITY")
        private String municipality;

        @Column(name = "DISTRICT")
        private String district;

        @Column(name = "COUNTRY")
        private String country;

        @Column(name = "URL_MINISITE_LEXUS")
        private String urlMinisiteLexus;

        @Column(name = "URL_EXTERNO")
        private String urlExterno;

        @Column(name = "URL_USADOS")
        private String urlUsados;

        @Column(name = "SALES_CODE")
        private String salesCode;

        @Column(name = "AFTERSALES_CODE")
        private String aftersalesCode;

        @Column(name = "RELATED_AFTERSALES_CODE")
        private String relatedAftersalesCode;

        @Column(name = "ID_MINISITE")
        private String idMinisite;

        @Column(name = "LOGO")
        private String logo;

        @Column(name = "MAP")
        private String map;

        @Column(name = "IMAGE")
        private String image;

        @Column(name = "DEALER_CODE")
        private String dealerCode;

        @Column(name = "TOYOTA_DEALER_CODE")
        private String toyotaDealerCode;

        @Column(name = "FACILITY_CODE")
        private String facilityCode;

        @Column(name = "STATUS")
        private Character status;

        @Column(name = "NRDOOR")
        private String nrdoor;

        @Column(name = "FLOOR")
        private String floor;

        @Column(name = "OID_NET")
        private String oidNet;

        @Column(name = "TARS_UOID")
        private String tarsUoid;

        @Column(name = "IS_CA_MEMBER")
        private Character isCaMember;

        @Column(name = "IS_GSC_MEMBER")
        private Character isGscMember;

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
        private Character hasSinage;

        @Column(name = "SINAGE_DT_IMPLEMENTED")
        private LocalDate sinageDtImplemented;

        @Column(name = "HAS_NCR")
        private Character hasNcr;

        @Column(name = "NCR_DT_IMPLEMENTED")
        private LocalDate ncrDtImplemented;

        @Column(name = "NIF")
        private String nif;

        @Column(name = "URL_DMS_WS")
        private String urlDmsWs;

        @Column(name = "OID_BRAND")
        private String oidBrand;

        @Column(name = "OID_TRADER")
        private String oidTrader;

        @Column(name = "INFO_TOTAL_AREA")
        private Double infoTotalArea;

        @Column(name = "INFO_SHOWROOM")
        private Double infoShowroom;

        @Column(name = "INFO_AFTER_SALES")
        private Double infoAfterSales;

        @Column(name = "EXT_MANAGING_COMPANY_ID")
        private String extManagingCompanyId;

        @Column(name = "EXT_HOLDING_ID")
        private String extHoldingId;

}
