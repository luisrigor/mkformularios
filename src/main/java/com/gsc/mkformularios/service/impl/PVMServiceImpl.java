package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.ApplicationConfiguration;
import com.gsc.mkformularios.config.datasource.DbClient;
import com.gsc.mkformularios.config.datasource.DbContext;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.entity.PVMCarmodel;
import com.gsc.mkformularios.model.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.PVMMonthlyReportDetailRepository;
import com.gsc.mkformularios.repository.PVMMonthlyReportRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import com.sc.commons.comunications.Mail;
import com.sc.commons.user.GSCUser;
import com.sc.commons.utils.DateTimerTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import com.rg.dealer.Dealer;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j
public class PVMServiceImpl implements PVMService {


    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;
    private final PVMMonthlyReportDetailRepository pvmMonthlyReportDetailRepository;

    public void setDataSourceContext(Long client) {
        if (client == 2L)
            dbContext.setBranchContext(DbClient.DB_LEXUS);
    }

    @Override
    public PVMDetailDTO getPVMDetail(int idPVM, UserPrincipal userPrincipal) {
        this.setDataSourceContext(userPrincipal.getClientId());
        List<PVMCarmodel> car = pvmCarmodelRepository.getCar();
        Optional<PVMMonthlyReport> monthlyReport = pvmMonthlyReportRepository.findById(idPVM);
        List<SalesPlates> salesAndPlates = pvmMonthlyReportRepository.getSalesAndPlates(idPVM);

        return PVMDetailDTO.builder()
                .car(car)
                .monthlyReport(monthlyReport.orElse(null))
                .salesAndPlates(salesAndPlates)
                .build();

    }

    @Override
    public Boolean newPVM(GSCUser oGSCUser, int subDealer) {
        boolean success;
        try{
            Optional<PVMMonthlyReport> data = pvmMonthlyReportRepository.newPVM(DateTimerTasks.getCurYear(), DateTimerTasks.getCurMonth(), oGSCUser.getOidDealerParent(), subDealer);
            if(data.isPresent()){
                log.info("PVM j� criado para o m�s actual");
                success = false;
            }else{
                PVMMonthlyReport oPVMMonthlyReport = insertMonthlyReport(oGSCUser,subDealer);
                pvmMonthlyReportDetailRepository.mergePVMMonthlyReportDetail(oPVMMonthlyReport.getId());
                success = true;
            }
        }catch (Exception ex) {
            //Revisar para aplicar SCErrorException
            log.error("oGSCUser.getLogin(),PVM,Erro ao criar nova Previs�o de Vendas Mensais" + ex.getMessage());
            throw new RuntimeException("An error occurred while getting Golden Record Relationships", ex);
        }
       return success;
    }

    @Override
    public void providePVMToDealer(UserPrincipal userPrincipal, String cancelReasons, int idPVM) {
        try {
            if (idPVM > 0) {
                this.setDataSourceContext(userPrincipal.getClientId());
                PVMMonthlyReport oPVMMonthlyReport = pvmMonthlyReportRepository.findById(idPVM).get();
                //Dealer oDealer = Dealer.getHelper().getByObjectId(oGSCUser.getOidNet(), oPVMMonthlyReport.getOidDealer());
                String reason = oPVMMonthlyReport.getReason();
                if (!reason.equals(""))
                    reason += "\n\n";

                oPVMMonthlyReport.setReason(reason + "Pedido de Devolu��o de PVM\nDe NMSC:\nPara: " + oDealer.getDesig() + "\nData: " + DateTimerTasks.Today() + "\nMotivo: " + cancelReasons + "\n\n");
                oPVMMonthlyReport.setAvailable(0);
                oPVMMonthlyReport.setDtAvailability(null);
                oPVMMonthlyReport.save(userPrincipal.getClientId());

                String strMail = "O PVM (Previs�o de Vendas/Matr�culas Mensais) de "+oPVMMonthlyReport.getYear()+"/"+oPVMMonthlyReport.getMonth()+" encontra-se novamente dispon�vel.\n\n"+"Motivos:\n"+reason+"\n\n\nPoder� consultar e dar sequ�ncia a este registo, no Portal da Extranet Toyota.\n\nCumprimentos,\nExtranet Toyota" + Mail.getFooterNoReply();
                String from = ApplicationConfiguration.getMailFrom(String.valueOf(userPrincipal.getClientId()));
               int idProfile = String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA) ?
                        ApplicationConfiguration.ID_PRF_TOYOTA_TCAP : ApplicationConfiguration.ID_PRF_LEXUS_TCAP;
                //agregar clase ultilitaria
                String to = UsrlogonUtil.getMailsForProfile(idProfile, oGSCUser.getOidDealerParent(), oGSCUser.getOidNet());
                String application = String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA) ? "Toyota" : "Lexus";
                Mail.SendMail(from, to, "", Mail.MAIL_ADDRESS_WEBNOFITICATIONS, "Extranet "+ application +" - PVM devolvido.", strMail);
            }
        } catch (Exception ex) {
            //validar para agregar SCErrorException
            log.error("PVM,Error ao devolver a Previs�o de Vendas Mensais" + ex.getMessage());
        }

    }

    public PVMMonthlyReport insertMonthlyReport(GSCUser oGSCUser, int subDealer){
        PVMMonthlyReport oPVMMonthlyReport = new PVMMonthlyReport();
        oPVMMonthlyReport.setYear(DateTimerTasks.getCurYear());
        oPVMMonthlyReport.setMonth(DateTimerTasks.getCurMonth());
        oPVMMonthlyReport.setOidDealer(oGSCUser.getOidDealerParent());
        oPVMMonthlyReport.setAvailable(0);
        oPVMMonthlyReport.setSubDealer(subDealer);
        return pvmMonthlyReportRepository.save(oPVMMonthlyReport);
    }
}
