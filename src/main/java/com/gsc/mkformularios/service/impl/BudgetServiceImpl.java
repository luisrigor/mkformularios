package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.PVMConstants;
import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.repository.toyota.PVMBudgetRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.BudgetService;
import com.gsc.mkformularios.utils.DealersUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;

@Service
@Log4j
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private final DbContext dbContext;
    private final PVMBudgetRepository pvmBudgetRepository;
    private final DealersUtils dealersUtils;


    public void setDataSourceContext(Long client) {
        if (client == APP_LEXUS) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == APP_TOYOTA) {
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    @Override
    public BudgetDTO editBudget(String yearSelect, UserPrincipal userPrincipal) {
        this.setDataSourceContext(userPrincipal.getClientId());

        List<PVMBudget> vecBudget = new ArrayList<>();
        int year = StringTasks.cleanInteger(yearSelect, Calendar.getInstance().get(Calendar.YEAR));

        try{
            vecBudget = pvmBudgetRepository.getPVMBudgetByYear(year);

            List<String[]> vecDealers = dealersUtils.getRetailerDealers(userPrincipal.getOidNet());
            vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_FROTAS_SUL 	+ "-[TCAP Frotas Sul]",			 	PVMConstants.DLR_CODE_FROTAS_SUL,    String.valueOf(PVMConstants.SUB_DLR_CODE_FROTAS_SUL)});
            vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_IMOBILIZADO 	+ "-[TCAP Imobilizado (Gaia)]", 	PVMConstants.DLR_CODE_IMOBILIZADO,   String.valueOf(PVMConstants.SUB_DLR_CODE_IMOBILIZADO)});
            vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_FROTAS_GAIA	+ "-[TCAP Frotas Gaia]",			PVMConstants.DLR_CODE_FROTAS_GAIA,	String.valueOf(PVMConstants.SUB_DLR_CODE_FROTAS_GAIA)});
            vecDealers.add(new String[]{Dealer.OID_NMSC, PVMConstants.DLR_CODE_EXPORTACAO	+ "-[TCAP Exporta��o (Ovar)]",		PVMConstants.DLR_CODE_EXPORTACAO,	String.valueOf(PVMConstants.SUB_DLR_CODE_EXPORTACAO)});

            return BudgetDTO.builder()
                    .dealers(vecDealers)
                    .budgets(vecBudget)
                    .year(year)
                    .build();

        } catch(Exception e){
            log.error("Error fetching budget ", e);
            throw new GetPVMException("Error fetching budget ", e);
        }
    }
}
