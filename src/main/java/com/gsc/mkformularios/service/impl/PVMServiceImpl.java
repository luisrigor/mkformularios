package com.gsc.mkformularios.service.impl;

import com.gsc.mkformularios.config.ApplicationConfiguration;
import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.dto.*;
import com.gsc.mkformularios.exceptions.CreatePVMException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.config.datasource.toyota.DbClient;
import com.gsc.mkformularios.config.datasource.toyota.DbContext;
import com.gsc.mkformularios.constants.AppProfile;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.SalesPlates;
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
import com.gsc.mkformularios.utils.UsrlogonUtil;
import com.gsc.mkformularios.repository.toyota.PVMMonthlyReportRepository;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import com.gsc.mkformularios.service.impl.pvm.PVM1MonthReport;
import com.gsc.mkformularios.service.impl.pvm.PVM3MonthReport;
import com.rg.dealer.Dealer;
import com.sc.commons.comunications.Mail;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import com.rg.dealer.Dealer;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final UsrlogonUtil usrlogonUtil;
    private final PVM3MonthReport pvm3MonthReport;
    private final PVM1MonthReport pvm1MonthReport;

    public void setDataSourceContext(Long client){
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
            List<DealerDTO> notSendPVMDealers = new ArrayList<>();

            if (userPrincipal.getRoles().contains(AppProfile.TCAP)) {
                List<String> notSendPVMOid = pvmMonthlyReportRepository.getNotSendPVMOid(pvmRequestDTO.getYear(), pvmRequestDTO.getMonth());
                notSendPVMOid.add("-1");
                if ("TOYOTA_RETAILER".equals(ApplicationConfiguration.getViewDealers(userPrincipal.getOidNet()))) {
                    List<ToyotaRetailer> allByObjectidNotIn = toyotaRetailerRepository.findAllByObjectidNotIn(notSendPVMOid);
                    notSendPVM = allByObjectidNotIn.stream().map(ToyotaRetailer::getObjectid).collect(Collectors.toList());
                } else if ("LEXUS_RETAILER".equals(ApplicationConfiguration.getViewDealers(userPrincipal.getOidNet()))) {
                    List<LexusRetailer> allByObjectidNotIn = lexusRetailerRepository.findAllByObjectIdNotIn(notSendPVMOid);
                    notSendPVM = allByObjectidNotIn.stream().map(LexusRetailer::getObjectId).collect(Collectors.toList());

                }
            }

            for (String currentNotPVM: notSendPVM) {
                List<DealerDTO> dealers = mapDealers.stream()
                        .filter(dealerDTO -> currentNotPVM.equals(dealerDTO.getKey()))
                        .collect(Collectors.toList());
                notSendPVMDealers.addAll(dealers);
            }

            return PVMGetDTO.builder()
                    .pvmMonthlyReports(pvmMonthlyReports)
                    .mapDealers(mapDealers)
                    .notSendPVM(notSendPVMDealers)
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
            log.error("oGSCUser.getLogin(),PVM,Erro ao criar nova Previs�o de Vendas Mensais" + ex.getMessage());
            throw new CreatePVMException("An error occurred while getting Golden Record Relationships", ex);
        }
       return success;
    }

    @Override
    public void providePVMToDealer(UserPrincipal userPrincipal, String cancelReasons, int idPVM) {
        cancelReasons = StringTasks.cleanString(cancelReasons, "");
        idPVM = StringTasks.cleanInteger(String.valueOf(idPVM), -1);
        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            PVMMonthlyReport oPVMMonthlyReport = pvmMonthlyReportRepository.findById(idPVM).orElseThrow(()->new GetPVMException("No record with id were found"));
            Dealer oDealer = dealersUtils.getDealerById(userPrincipal.getOidNet(), oPVMMonthlyReport.getOidDealer());
            String reason = oPVMMonthlyReport.getReason();
            if (!reason.equals(""))
                reason += "\n\n";

            oPVMMonthlyReport.setReason(reason + "Pedido de Devolução de PVM\nDe NMSC:\nPara: " + oDealer.getDesig() + "\nData: " + DateTimerTasks.Today() + "\nMotivo: " + cancelReasons + "\n\n");
            oPVMMonthlyReport.setAvailable(0);
            oPVMMonthlyReport.setDtAvailability(null);
            pvmMonthlyReportRepository.save(oPVMMonthlyReport);

            String strMail = "O PVM (Previsão de Vendas/Matrículas Mensais) de "+oPVMMonthlyReport.getYear()+"/"+oPVMMonthlyReport.getMonth()+" í\n\n"+"Motivos:\n"+reason+"\n\n\nPoderá consultar e dar sequência a este registo, no Portal da Extranet Toyota.\n\nCumprimentos,\nExtranet Toyota" + Mail.getFooterNoReply();
            this.selectMailInfo(userPrincipal.getOidNet(), userPrincipal.getOidDealerParent(),-1, -1, oPVMMonthlyReport.getReason(), strMail);
        } catch (Exception ex) {
            log.error("PVM, Error returning the Monthly Sales Forecast ", ex);
            throw  new GetPVMException("Error returning the Monthly Sales Forecast ", ex);

        }

    }

    @Transactional
    @Override
    public void saveReportDetail(UserPrincipal userPrincipal, List<ReportDetailRequestDto> reportDetailRequestDto, String idPVMS) {
        this.setDataSourceContext(userPrincipal.getClientId());
        int idPVM = StringTasks.cleanInteger(idPVMS, -1);
        String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];

        try {
            for (ReportDetailRequestDto currentReportDetail: reportDetailRequestDto) {
                this.updateReportDetail(currentReportDetail, userStamp, idPVM);
            }
        } catch (Exception e) {
            log.error("Error saving monthly sales forecasts", e);
            throw new CreatePVMException("Error saving monthly sales forecasts", e);
        }
    }

    @Transactional
    @Override
    public void sendReportDetail(UserPrincipal userPrincipal, List<ReportDetailRequestDto> reportDetailRequestDto, String idPVMS) {
        this.setDataSourceContext(userPrincipal.getClientId());
        int idPVM = StringTasks.cleanInteger(idPVMS, -1);
        String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];

        try {
            for (ReportDetailRequestDto currentReportDetail: reportDetailRequestDto) {
                this.updateReportDetail(currentReportDetail, userStamp, idPVM);
            }
            Optional<PVMMonthlyReport> monthlyReport = pvmMonthlyReportRepository.findById(idPVM);
            if(monthlyReport.isPresent()) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                PVMMonthlyReport monthlyReportId = monthlyReport.get();
                monthlyReportId.setAvailable(1);
                monthlyReportId.setDtAvailability(ts.toLocalDateTime());
                pvmMonthlyReportRepository.save(monthlyReportId);
            }
        } catch (Exception e) {
            log.error("Error saving monthly sales forecast", e);
            throw new CreatePVMException("Error saving monthly sales forecast", e);
        }
    }

    public void requestToChange(UserPrincipal userPrincipal, String cancelReasons, String idPVMs) {
        cancelReasons = StringTasks.cleanString(cancelReasons, "");
        int idPVM = StringTasks.cleanInteger(idPVMs, -1);

        try {
            this.setDataSourceContext(userPrincipal.getClientId());
            PVMMonthlyReport oPVMMonthlyReport = pvmMonthlyReportRepository.findById(idPVM).orElseThrow(()->new GetPVMException("No report with id " +idPVM+ "were found"));
            Dealer oDealer = dealersUtils.getDealerById(userPrincipal.getOidNet(), oPVMMonthlyReport.getOidDealer());
            String reason = oPVMMonthlyReport.getReason();
            int year = oPVMMonthlyReport.getYear();
            int month = oPVMMonthlyReport.getMonth();
            if (!reason.equals(""))
                reason += "\n\n";

            oPVMMonthlyReport.setReason(reason +"Pedido de Devolução de PVM\nDe: "+ oDealer.getDesig() + "\nPara: NMSC\nData: "+ DateTimerTasks.Today() + "\nMotivo: " + cancelReasons + "\n\n");
            //grava reason
            oPVMMonthlyReport = pvmMonthlyReportRepository.save(oPVMMonthlyReport);
            this.selectMailInfo(userPrincipal.getOidNet(), userPrincipal.getOidDealerParent(),year, month, oPVMMonthlyReport.getReason(), null);
        } catch (Exception e) {
            log.error("Error returning the Monthly Sales Forecast", e);
            throw  new GetPVMException("Error returning the Monthly Sales Forecast", e);
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

    @Transactional
    public void updateReportDetail(ReportDetailRequestDto currentReportDetail, String userStamp, Integer idPVM) {
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

        pvmMonthlyReportDetailRepository.updateReportDetail(userStamp, ts.toLocalDateTime(), sales1, plates1, sales2, sales3, plates2, plates3, contract, vdvc, vdvc2, vdvc3, idPVM, idModel);
    }


    private void selectMailInfo(String oidNet, String oidDealerParent, int year, int month, String reason, String strMail) throws SCErrorException {
        int idProfile;
        String from = ApplicationConfiguration.getMailFrom(oidNet);
        if(oidNet.equals(Dealer.OID_NET_TOYOTA)) {
            strMail = strMail==null?"Foi pedida a devolução do PVM (Previsão de Vendas/Matrículas Mensais) de "+year+"/"+month+"\n\nMotivos:\n"+reason+"\n\n\nPoderá consultar e dar sequência a este registo, no Portal da Extranet Toyota.\n\nCumprimentos,\nExtranet Toyota" + Mail.getFooterNoReply()
                    :strMail;
            idProfile = ApplicationConfiguration.ID_PRF_TOYOTA_DEALER;
            String to = usrlogonUtil.getMailsForProfile(idProfile, oidDealerParent, oidNet);
            Mail.SendMail(from, to, "", Mail.MAIL_ADDRESS_WEBNOFITICATIONS, "Extranet Toyota - PVM devolvido.", strMail);
        } else if (oidNet.equals(Dealer.OID_NET_LEXUS)) {
            strMail = strMail==null?"Foi pedida a devolução do PVM (Previsão  de Vendas/Matrículas Mensais) de "+year+"/"+month+"\n\nMotivos:\n"+reason+"\n\n\nPoderá consultar e dar sequência a este registo, no Portal da Extranet Lexus.\n\nCumprimentos,\nExtranet Lexus" + Mail.getFooterNoReply():
                    strMail;
            idProfile = ApplicationConfiguration.ID_PRF_LEXUS_DEALER;
            String to = usrlogonUtil.getMailsForProfile(idProfile, oidDealerParent, oidNet);
            Mail.SendMail(from, to, "", Mail.MAIL_ADDRESS_WEBNOFITICATIONS, "Extranet Lexus - PVM devolvido.", strMail);
        }
    }

    @Override
    public void getPVMExcelByMonth(PVMRequestDTO pvmRequestDTO, String pvmMonth, UserPrincipal userPrincipal, HttpServletResponse response) {
        //TODO remove this user init
        userPrincipal = new UserPrincipal(null, null, null);
        userPrincipal.setCaMember(false);
        int year	= pvmRequestDTO.getYear();
        int month	= pvmRequestDTO.getMonth();
        String oidNet = StringTasks.cleanString(pvmRequestDTO.getOidNet(), "");
        if (year==0 || month==0)
            return;

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String filename = "Previsão_de_Vendas_e_Matrículas_(" +DateTimerTasks.ptMonths[month-1]+ ").xls";
        String headerValue = "attachment; filename="+filename;
        response.setHeader(headerKey, headerValue);

        try {
            HSSFWorkbook oHSSFWorkbook = null;

            if ("1MONTH".equals(pvmMonth)) {
                if (oidNet.equals(Dealer.OID_NET_TOYOTA))	{
                    oHSSFWorkbook = pvm1MonthReport.execute(year, month, "Toyota", userPrincipal.isCAMember());
                } else if (oidNet.equals(Dealer.OID_NET_LEXUS))	{
                    oHSSFWorkbook = pvm1MonthReport.execute(year, month, "Lexus", userPrincipal.isCAMember());
                }
            } else if ("3MONTH".equals(pvmMonth)) {
                if (oidNet.equals(Dealer.OID_NET_TOYOTA))	{
                    oHSSFWorkbook = pvm3MonthReport.executeForToyota(year, month, userPrincipal.isCAMember());
                } else if (oidNet.equals(Dealer.OID_NET_LEXUS))	{
                    oHSSFWorkbook = pvm3MonthReport.executeForLexus(year, month, userPrincipal.isCAMember());
                }
            }

            ServletOutputStream outputStream = response.getOutputStream();
            oHSSFWorkbook.write(outputStream);
            oHSSFWorkbook.close();
            outputStream.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating file", e);
        }
    }
}
