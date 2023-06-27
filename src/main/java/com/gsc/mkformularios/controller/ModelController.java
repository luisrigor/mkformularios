package com.gsc.mkformularios.controller;

import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.GoToModelDTO;
import com.gsc.mkformularios.dto.ModelDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.ModelService;
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
@Api(value = "", tags = "MODEL")
@RestController
@CrossOrigin("*")
public class ModelController {


    private final ModelService modelService;

    @GetMapping(PVMEnpoints.MODEL_GOTO)
    public ResponseEntity<GoToModelDTO> getModel(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                 @RequestParam boolean isDetail, @RequestParam Integer idModel, @RequestParam String year) {
        log.info("Client id " + userPrincipal.getClientId());
        GoToModelDTO goToModel= modelService.goToModel(userPrincipal, isDetail, idModel, year);
        return ResponseEntity.status(HttpStatus.OK).body(goToModel);
    }

    @PostMapping(PVMEnpoints.MODEL_SAVE)
    public ResponseEntity<Boolean> saveModel(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                             @RequestBody ModelDTO model, @RequestParam int idModel) {
        log.info("Client id " + userPrincipal.getClientId());
        Boolean saved= modelService.saveModel(userPrincipal, model, idModel);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }
}
