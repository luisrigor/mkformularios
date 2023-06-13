package com.gsc.mkformularios.controller;


import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PVMService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}"+PVMEnpoints.PVM_BASE)
@Api(value = "",tags = "PVM")
@RestController
@CrossOrigin("*")
public class PVMController {

    private final PVMService PVMService;

    @GetMapping(PVMEnpoints.PVM_DETAIL)
    public ResponseEntity<PVMDetailDTO> getPVMDetail(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestParam int idPVM) {
        log.info("fetching PVMDetail with id " + idPVM);
        log.info("Client id " +userPrincipal.getClientId());
        PVMDetailDTO pvmDetail = PVMService.getPVMDetail(idPVM, userPrincipal);
        return  ResponseEntity.status(HttpStatus.OK).body(pvmDetail);
    }
}
