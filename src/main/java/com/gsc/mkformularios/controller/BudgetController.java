package com.gsc.mkformularios.controller;

import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.BudgetService;
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
@Api(value = "", tags = "BUDGET")
@RestController
@CrossOrigin("*")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping(PVMEnpoints.BUDGET_EDIT)
    public ResponseEntity<BudgetDTO> editBudget(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String year) {
        BudgetDTO budgetDTO = budgetService.editBudget(year, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(budgetDTO);
    }
}
