package com.gsc.mkformularios.controller;

import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PlanService;
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
@Api(value = "", tags = "PLAN")
@RestController
@CrossOrigin("*")
public class PlanController {

    private final PlanService planService;

    @GetMapping(PVMEnpoints.PLAN_EDIT)
    public ResponseEntity<PlanDTO> editPlan(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String year) {
        PlanDTO planDTO = planService.goToEditPlan(userPrincipal, year);
        return ResponseEntity.status(HttpStatus.OK).body(planDTO);

    }
}
