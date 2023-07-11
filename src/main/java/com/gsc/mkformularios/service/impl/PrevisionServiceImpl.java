package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.ApplicationConfiguration;
import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.dto.PrevisionDTO;
import com.gsc.mkformularios.model.toyota.entity.PrevisionFilterBean;
import com.gsc.mkformularios.model.toyota.entity.UsedCarsPrevisionSales;
import com.gsc.mkformularios.repository.toyota.PrevisionRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PrevisionService;
import com.rg.dealer.Dealer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.*;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;

@Service
@RequiredArgsConstructor
@Log4j
public class PrevisionServiceImpl implements PrevisionService {

    public static final String ANNUAL = "ANUAL";
    public static final String MONTHLY = "MENSAL";
    public static final String EXCEL = "EXCELL";
    private final DbContext dbContext;
    private final PrevisionFilterBean filterBean;

    private final PrevisionRepository previsionRepository;

    public void setDataSourceContext(Long client) {
        if (client == APP_LEXUS) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == APP_TOYOTA) {
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    @Override
    public PrevisionDTO getUsedCarsAllPrevisionSalesMonth(UserPrincipal userPrincipal) {
        Vector<Dealer> vecDealers = null;
        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            if (userPrincipal.getRoles().toString().contains(ApplicationConfiguration.TVC_MANAGER_ROLE_ACTIVE_DEALERS)) {
                vecDealers = Dealer.getHelper().getActiveMainDealersForServices(userPrincipal.getOidNet(), Dealer.OID_NET_TOYOTA.equals(userPrincipal.getOidNet()) ? Dealer.OID_TOYOTA_SERVICE_USEDCARS : Dealer.OID_LEXUS_SERVICE_USEDCARS);
            }
            List<UsedCarsPrevisionSales> hstMonthPrevision = usedCarsPrevision(filterBean.getIvYear(), filterBean.getIvMonth(), MONTHLY, UsedCarsPrevisionSales.PREVISION_TYPE_MENSAL);
            List<UsedCarsPrevisionSales> hstMonthPrevisionTcap = usedCarsPrevision(filterBean.getIvYear(), filterBean.getIvMonth(), MONTHLY, UsedCarsPrevisionSales.PREVISION_TYPE_ANUAL);
            return PrevisionDTO.builder()
                    .dealers(vecDealers)
                    .hstMonthPrevisionConc(hstMonthPrevision)
                    .hstMonthPrevisionTcap(hstMonthPrevisionTcap)
                    .build();
        } catch (Exception e) {
            log.error("Visualizar Previs�o de Vendas de Usados Erro ao visualizar formul�rio de Previs�o de Vendas de Usados");
            throw new RuntimeException("Erro ao visualizar formul�rio de Previs�o de Vendas de Usados");
        }
    }

    private List<UsedCarsPrevisionSales> usedCarsPrevision(Integer year, Integer month, String reportType, String previsionType){
        UsedCarsPrevisionSales oUsedCarsPrevisionSales = null;
        List<UsedCarsPrevisionSales> usedCars;
        List<UsedCarsPrevisionSales> hstUsedCarsPrevisionSales = new ArrayList<>();
        usedCars = ANNUAL.equals(reportType)
                     ? previsionRepository.previsionAnnual(year, month, reportType, previsionType)
                     : otherPrevision(year, month, reportType, previsionType);
        for(UsedCarsPrevisionSales data : usedCars){
            if(ANNUAL.equals(reportType)){
                oUsedCarsPrevisionSales = UsedCarsPrevisionSales.builder()
                        .ivOidDealer(data.getIvOidDealer())
                        .ivPrevisionTvc(data.getIvPrevisionTvc())
                        .ivPrevisionSn(data.getIvPrevisionSn())
                        .build();
            }else{
                //oUsedCarsPrevisionSales = (UsedCarsPrevisionSales)rowToObject(rs);
            }
            hstUsedCarsPrevisionSales.add(oUsedCarsPrevisionSales);
        }
        return hstUsedCarsPrevisionSales;
    }

    private List<UsedCarsPrevisionSales> otherPrevision(Integer year,Integer month,String reportType,String previsionType){
       return EXCEL.equals(reportType)
                ? previsionRepository.previsionExcell(year, month, reportType, previsionType)
                : previsionRepository.previsionMonthly(year, month, reportType, previsionType);
    }
}
