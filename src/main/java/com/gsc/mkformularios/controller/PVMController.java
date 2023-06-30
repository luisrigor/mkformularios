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

import java.util.List;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}" + PVMEnpoints.PVM_BASE)
@Api(value = "", tags = "PVM")
@RestController
@CrossOrigin("*")
public class PVMController {

    private final PVMService PVMService;

    @PostMapping(PVMEnpoints.PVM_GOTO_PVM)
    public ResponseEntity<?> getPVM(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody PVMRequestDTO pvmRequestDTO) {
        log.info("Client id " +userPrincipal.getClientId());
        PVMGetDTO pvm = PVMService.getPVM(pvmRequestDTO, userPrincipal);
        Gson gson = new Gson();
        return  ResponseEntity.status(HttpStatus.OK).body(gson.toJson(pvm));
    }

    @GetMapping(PVMEnpoints.PVM_DETAIL)
    public ResponseEntity<PVMDetailDTO> getPVMDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam int idPVM) {
        log.info("fetching PVMDetail with id " + idPVM);
        log.info("Client id " + userPrincipal.getClientId());
        PVMDetailDTO pvmDetail = PVMService.getPVMDetail(idPVM, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(pvmDetail);
    }

    @PostMapping(PVMEnpoints.PVM_NEW)
    public ResponseEntity<String> newPVM(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam int subDealer) {
        log.info("Client id " + userPrincipal.getClientId());
        PVMService.newPVM(userPrincipal, subDealer);
        return ResponseEntity.status(HttpStatus.OK).body("pvmDetail");
    }

    @PostMapping(PVMEnpoints.PVM_SAVE_REPORT_DETAIL)
    public ResponseEntity<String> saveReportDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @RequestBody List<ReportDetailRequestDto> reportDetailList, @RequestParam String idPVMS) {
        log.info("Client id " + userPrincipal.getClientId());
        PVMService.saveReportDetail(userPrincipal, reportDetailList, idPVMS);
        return ResponseEntity.status(HttpStatus.OK).body("pvmDetail");
    }




}
