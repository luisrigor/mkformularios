package com.gsc.mkformularios.controller;


import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}"+PVMEnpoints.PVM_BASE)
@Api(value = "",tags = "PVM")
@RestController
@CrossOrigin("*")
public class PVMController {

    private final PVMService PVMService;

    @PostMapping(PVMEnpoints.PVM_GOTO_PVM)
    public ResponseEntity<PVMDetailDTO> getPVM(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestBody PVMRequestDTO pvmRequestDTO) {
        log.info("Client id " +userPrincipal.getClientId());
        PVMDetailDTO pvmDetail = PVMService.getPVM(pvmRequestDTO, userPrincipal);
        return  ResponseEntity.status(HttpStatus.OK).body(pvmDetail);
    }

    @GetMapping(PVMEnpoints.PVM_DETAIL)
    public ResponseEntity<PVMDetailDTO> getPVMDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam int idPVM) {
        log.info("fetching PVMDetail with id " + idPVM);
        log.info("Client id " +userPrincipal.getClientId());
        PVMDetailDTO pvmDetail = PVMService.getPVMDetail(idPVM, userPrincipal);
        return  ResponseEntity.status(HttpStatus.OK).body(pvmDetail);
    }

    @PostMapping(PVMEnpoints.PVM_EXPORT_MONTH)
    public ResponseEntity<String> getPVMExcelByMonth(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletResponse response,
                                                     @RequestBody PVMRequestDTO pvmRequestDTO, @RequestParam String month) {
//        log.info("Client id " +userPrincipal.getClientId());
        PVMService.getPVMExcelByMonth(pvmRequestDTO, month,userPrincipal, response);
        return  ResponseEntity.status(HttpStatus.OK).body("excel generated");
    }

}
