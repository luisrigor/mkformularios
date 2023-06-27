package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.ApplicationConfiguration;
import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.dto.*;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toynet.entity.LexusRetailer;
import com.gsc.mkformularios.model.toynet.entity.ToyotaRetailer;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.repository.toynet.LexusRetailerRepository;
import com.gsc.mkformularios.repository.toynet.ToyotaRetailerRepository;
import com.gsc.mkformularios.repository.toyota.PVMCarmodelRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportDetailRepository;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import com.gsc.mkformularios.utils.DealersUtils;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import com.rg.dealer.Dealer;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.gsc.mkformularios.constants.DATAConstants.APP_LEXUS;
import static com.gsc.mkformularios.constants.DATAConstants.APP_TOYOTA;

@RequiredArgsConstructor
@Service
@Log4j
public class PVMServiceImpl implements PVMService {


    private final DbContext dbContext;
    private final PVMCarmodelRepository pvmCarmodelRepository;
    private final PVMMonthlyReportRepository pvmMonthlyReportRepository;
    private final ToyotaRetailerRepository toyotaRetailerRepository;
    private final LexusRetailerRepository lexusRetailerRepository;
    private final PVMMonthlyReportDetailRepository pvmMonthlyReportDetailRepository;
    private final DealersUtils dealersUtils;

    public void setDataSourceContext(Long client) {
        if (client == APP_LEXUS) {
            dbContext.setBranchContext(DbClient.DB_LEXUS);
        } else if (client == APP_TOYOTA) {
            dbContext.setBranchContext(DbClient.DB_TOYOTA);
        }
    }

    @Override
    public PVMGetDTO getPVM(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal) {
        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            List<PVMMonthlyReport> pvmMonthlyReports = pvmMonthlyReportRepository.getPVM(pvmRequestDTO, userPrincipal);
            List<DealerDTO> mapDealers = dealersUtils.getMapDealers(userPrincipal.getOidNet());
            List<String> notSendPVM = new ArrayList<>();

            if (userPrincipal.getRoles().contains(AppProfile.TCAP)) {
                List<String> notSendPVMOid = pvmMonthlyReportRepository.getNotSendPVMOid(pvmRequestDTO.getYear(), pvmRequestDTO.getMonth());
                if ("TOYOTA_RETAILER".equals(ApplicationConfiguration.getViewDealers(userPrincipal.getOidNet()))) {
                    List<ToyotaRetailer> allByObjectidNotIn = toyotaRetailerRepository.findAllByObjectidNotIn(notSendPVMOid);
                    notSendPVM = allByObjectidNotIn.stream().map(ToyotaRetailer::getObjectid).collect(Collectors.toList());
                } else if ("LEXUS_RETAILER".equals(ApplicationConfiguration.getViewDealers(userPrincipal.getOidNet()))) {
                    List<LexusRetailer> allByObjectidNotIn = lexusRetailerRepository.findAllByObjectIdNotIn(notSendPVMOid);
                    notSendPVM = allByObjectidNotIn.stream().map(LexusRetailer::getObjectId).collect(Collectors.toList());

                }
            }

            return PVMGetDTO.builder()
                    .pvmMonthlyReports(pvmMonthlyReports)
                    .mapDealers(mapDealers)
                    .notSendPVM(notSendPVM)
                    .build();

        }catch (Exception ex) {
            log.error("Error fetching pvm", ex);
            throw  new GetPVMException("Error fetching pvm", ex);
        }
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
    public Boolean newPVM(UserPrincipal userPrincipal, int subDealer) {
        this.setDataSourceContext(userPrincipal.getClientId());
        boolean success;
        try{
            Optional<PVMMonthlyReport> data = pvmMonthlyReportRepository.newPVM(DateTimerTasks.getCurYear(), DateTimerTasks.getCurMonth(), userPrincipal.getOidDealerParent(), subDealer);
            if(data.isPresent()){
                log.info("PVM j� criado para o m�s actual");
                success = false;
            }else{
                PVMMonthlyReport oPVMMonthlyReport = insertMonthlyReport(userPrincipal, subDealer);
                pvmMonthlyReportDetailRepository.mergePVMMonthlyReportDetail(oPVMMonthlyReport.getId());
                success = true;
            }
        }catch (Exception ex) {
            //Revisar para aplicar SCErrorException
            log.error("oGSCUser.getLogin(),PVM,Erro ao criar nova Previs�o de Vendas Mensais" + ex.getMessage());
            throw new CreatePVMException("An error occurred while getting Golden Record Relationships", ex);
        }
       return success;
    }

    @Override
    public void providePVMToDealer(UserPrincipal userPrincipal, String cancelReasons, int idPVM) {
        try {
            if (idPVM > 0) {
                this.setDataSourceContext(userPrincipal.getClientId());
                PVMMonthlyReport oPVMMonthlyReport = pvmMonthlyReportRepository.findById(idPVM).get();
                Dealer oDealer = Dealer.getHelper().getByObjectId(userPrincipal.getOidNet(), oPVMMonthlyReport.getOidDealer());
                String reason = oPVMMonthlyReport.getReason();
                if (!reason.equals(""))
                    reason += "\n\n";

                oPVMMonthlyReport.setReason(reason + "Pedido de Devolu��o de PVM\nDe NMSC:\nPara: " + oDealer.getDesig() + "\nData: " + DateTimerTasks.Today() + "\nMotivo: " + cancelReasons + "\n\n");
                oPVMMonthlyReport.setAvailable(0);
                oPVMMonthlyReport.setDtAvailability(null);
                pvmMonthlyReportRepository.save(oPVMMonthlyReport);

//                String strMail = "O PVM (Previs�o de Vendas/Matr�culas Mensais) de "+oPVMMonthlyReport.getYear()+"/"+oPVMMonthlyReport.getMonth()+" encontra-se novamente dispon�vel.\n\n"+"Motivos:\n"+reason+"\n\n\nPoder� consultar e dar sequ�ncia a este registo, no Portal da Extranet Toyota.\n\nCumprimentos,\nExtranet Toyota" + Mail.getFooterNoReply();
//                String from = ApplicationConfiguration.getMailFrom(String.valueOf(userPrincipal.getClientId()));
//               int idProfile = String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA) ?
//                        ApplicationConfiguration.ID_PRF_TOYOTA_TCAP : ApplicationConfiguration.ID_PRF_LEXUS_TCAP;
                //agregar clase ultilitaria
//                String to = UsrlogonUtil.getMailsForProfile(idProfile, oGSCUser.getOidDealerParent(), oGSCUser.getOidNet());
//                String application = String.valueOf(userPrincipal.getClientId()).equals(Dealer.OID_NET_TOYOTA) ? "Toyota" : "Lexus";
//                Mail.SendMail(from, to, "", Mail.MAIL_ADDRESS_WEBNOFITICATIONS, "Extranet "+ application +" - PVM devolvido.", strMail);
            }
        } catch (Exception ex) {
            //validar para agregar SCErrorException
            log.error("PVM,Error ao devolver a Previs�o de Vendas Mensais" + ex.getMessage());
        }

    }

    public void saveReportDetail(UserPrincipal userPrincipal, List<ReportDetailRequestDto> reportDetailRequestDto, String idPVMS) {
        this.setDataSourceContext(userPrincipal.getClientId());
        int idPVM = StringTasks.cleanInteger(idPVMS, -1);
        String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];

        try {
            for (ReportDetailRequestDto currentReportDetail: reportDetailRequestDto) {

                int idModel = currentReportDetail.getIdModel();
                int contract = StringTasks.cleanInteger(currentReportDetail.getContract(),0);
                int sales1 = StringTasks.cleanInteger(currentReportDetail.getS1(),0);
                int sales2 = StringTasks.cleanInteger(currentReportDetail.getS2(),0);
                int sales3 = StringTasks.cleanInteger(currentReportDetail.getS3(),0);
                int plates1 = StringTasks.cleanInteger(currentReportDetail.getP1(),0);
                int plates2 = StringTasks.cleanInteger(currentReportDetail.getP2(),0);
                int plates3 = StringTasks.cleanInteger(currentReportDetail.getP3(),0);

                int vdvc = StringTasks.cleanInteger(currentReportDetail.getV1(), 0);
                int vdvc2 = StringTasks.cleanInteger(currentReportDetail.getV2(), 0);
                int vdvc3 = StringTasks.cleanInteger(currentReportDetail.getV3(), 0);
                Timestamp ts = new Timestamp(System.currentTimeMillis());

//                pvmMonthlyReportDetailRepository.updateReportDetail(userStamp, ts.toLocalDateTime(), sales1, plates1, sales2, sales3, plates2, plates3, contract, vdvc, vdvc2, vdvc3, idPVM, idModel);
            }
        } catch (Exception e) {
            log.error("Error saving monthly sales forecast", e);
            throw new CreatePVMException("Error saving monthly sales forecast", e);
        }
    }



    public PVMMonthlyReport insertMonthlyReport(UserPrincipal userPrincipal, int subDealer){
        String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];
        PVMMonthlyReport oPVMMonthlyReport = new PVMMonthlyReport();
        oPVMMonthlyReport.setYear(DateTimerTasks.getCurYear());
        oPVMMonthlyReport.setMonth(DateTimerTasks.getCurMonth());
        oPVMMonthlyReport.setOidDealer(userPrincipal.getOidDealerParent());
        oPVMMonthlyReport.setCreatedBy(userStamp);
        oPVMMonthlyReport.setDtCreated(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
        oPVMMonthlyReport.setAvailable(0);
        oPVMMonthlyReport.setSubDealer(subDealer);
        return pvmMonthlyReportRepository.save(oPVMMonthlyReport);
    }

}
