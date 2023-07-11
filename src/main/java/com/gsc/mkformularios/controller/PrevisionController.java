package com.gsc.mkformularios.controller;

import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PrevisionDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PrevisionService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}" + PVMEnpoints.PVM_BASE)
@Api(value = "", tags = "PREVISION")
@RestController
@CrossOrigin("*")
public class PrevisionController {

    private final PrevisionService previsionService;

    @GetMapping(PVMEnpoints.PREVISION_MONTH)
    public ResponseEntity<PrevisionDTO> getUsedCarsAllPrevisionSalesMonth(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Client id " + userPrincipal.getClientId());
        PrevisionDTO previsionInfo= previsionService.getUsedCarsAllPrevisionSalesMonth(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(previsionInfo);
    }
}
