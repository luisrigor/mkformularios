package com.gsc.mkformularios.controller;


import com.google.gson.Gson;
import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.*;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.ModelService;
import com.gsc.mkformularios.service.PVMService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}" + PVMEnpoints.PVM_BASE)
@Api(value = "", tags = "PVM")
@RestController
@CrossOrigin("*")
public class PVMController {

    private final PVMService PVMService;
    private final String SAVED = "saved";

    @PostMapping(PVMEnpoints.PVM_GOTO_PVM)
    public ResponseEntity<?> getPVM(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody PVMRequestDTO pvmRequestDTO) {
        log.info("getPVM controller" +userPrincipal.getClientId());
        PVMGetDTO pvm = PVMService.getPVM(pvmRequestDTO, userPrincipal);
        Gson gson = new Gson();
        return  ResponseEntity.status(HttpStatus.OK).body(gson.toJson(pvm));
    }

    @GetMapping(PVMEnpoints.PVM_DETAIL)
    public ResponseEntity<PVMDetailDTO> getPVMDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam int idPVM) {
        log.info("fetching PVMDetail with id " + idPVM);
        log.info("getPVMDetail controller " + userPrincipal.getClientId());
        PVMDetailDTO pvmDetail = PVMService.getPVMDetail(idPVM, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(pvmDetail);
    }

    @PostMapping(PVMEnpoints.PVM_NEW)
    public ResponseEntity<Integer> newPVM(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam int subDealer) {
        log.info("newPVM controller " + userPrincipal.getClientId());
        Integer id = PVMService.newPVM(userPrincipal, subDealer);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    @PostMapping(PVMEnpoints.PVM_SAVE_REPORT_DETAIL)
    public ResponseEntity<String> saveReportDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestBody List<ReportDetailRequestDto> reportDetailList, @RequestParam String idPVMS) {
        log.info("saveReportDetail controller " + userPrincipal.getClientId());
        PVMService.saveReportDetail(userPrincipal, reportDetailList, idPVMS);
        return ResponseEntity.status(HttpStatus.OK).body(SAVED);
    }

    @PostMapping(PVMEnpoints.PVM_PROVIDE_TO_DEALER)
    public ResponseEntity<String> provideToDealer(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @RequestParam String cancelReasons, @RequestParam int idPVM) {
        log.info("provideToDealer controller " + userPrincipal.getClientId());
        PVMService.providePVMToDealer(userPrincipal, cancelReasons, idPVM);
        return ResponseEntity.status(HttpStatus.OK).body(SAVED);
    }

    @PostMapping(PVMEnpoints.PVM_REQUEST_TO_CHANGE)
    public ResponseEntity<String> requestToChange(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestParam String cancelReasons, @RequestParam String idPVMS) {
        log.info("requestToChange controller " + userPrincipal.getClientId());
        PVMService.requestToChange(userPrincipal, cancelReasons, idPVMS);
        return ResponseEntity.status(HttpStatus.OK).body(SAVED);
    }

    @PostMapping(PVMEnpoints.PVM_SEND_REPORTDETAIL)
    public ResponseEntity<String> sendReportDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestBody List<ReportDetailRequestDto> reportDetailList, @RequestParam String idPVMS) {
        log.info("sendReportDetail controller " + userPrincipal.getClientId());
        PVMService.sendReportDetail(userPrincipal, reportDetailList, idPVMS);
        return ResponseEntity.status(HttpStatus.OK).body(SAVED);
    }



    @PostMapping(PVMEnpoints.PVM_EXPORT_MONTH)
    public ResponseEntity<String> getPVMExcelByMonth(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletResponse response,
                                                     @RequestBody PVMRequestDTO pvmRequestDTO, @RequestParam String month) {
//        log.info("Client id " +userPrincipal.getClientId());
        PVMService.getPVMExcelByMonth(pvmRequestDTO, month,userPrincipal, response);
        return  ResponseEntity.status(HttpStatus.OK).body("excel generated");
    }

    @PostMapping(PVMEnpoints.GET_OID_NET)
    public ResponseEntity<String> getOidNet(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        String oidNet = PVMService.getOidNet(userPrincipal);
        return  ResponseEntity.status(HttpStatus.OK).body(oidNet);
    }

}
