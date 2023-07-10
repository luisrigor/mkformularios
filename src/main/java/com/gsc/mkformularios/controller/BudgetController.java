package com.gsc.mkformularios.controller;

import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.BudgetService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

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

    @PostMapping(PVMEnpoints.BUDGET_SAVE)
    public ResponseEntity<String> saveBudget(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String yearSelect,
                                             @RequestBody List<PVMBudget> lstPVMBudget) {
        budgetService.saveBudget(yearSelect, userPrincipal, lstPVMBudget);
        return ResponseEntity.status(HttpStatus.OK).body("saved");
    }

    @GetMapping(PVMEnpoints.BUDGET_DOWNLOAD)
    public ResponseEntity<String> downloadBudget(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam String yearBudget,
                                                 HttpServletResponse response) {

        try {
            List<String[]> data = budgetService.downloadBudget(yearBudget, userPrincipal, response);
            CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(response.getOutputStream()));
            csvWriter.writeAll(data);
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new GetPVMException("Error generating csv file", e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("generated");
    }

    @PostMapping(PVMEnpoints.BUDGET_UPLOAD)
    public ResponseEntity<String> uploadBudget(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @RequestParam String yearBudget, @RequestPart MultipartFile file) {
        budgetService.uploadBudget(yearBudget, userPrincipal, file);
        return ResponseEntity.status(HttpStatus.OK).body("uploaded");


    }


}
