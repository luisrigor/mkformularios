package com.gsc.mkformularios.controller;

import com.gsc.mkformularios.constants.api.PVMEnpoints;
import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.dto.UploadPlanResponseDTO;
import com.gsc.mkformularios.exceptions.FileUploadException;
import com.gsc.mkformularios.exceptions.GetPVMException;
import com.gsc.mkformularios.security.UserPrincipal;
import com.gsc.mkformularios.service.PlanService;
import com.opencsv.CSVWriter;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.List;

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

    @GetMapping(PVMEnpoints.PLAN_DOWNLOAD)
    public ResponseEntity<String> downloadPlan(@RequestParam String yearPlan,
                                                     @AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletResponse response) {
        try {
            List<String[]> data = planService.downloadPlan(yearPlan, userPrincipal, response);
            CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(response.getOutputStream()));
            csvWriter.writeAll(data);
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new GetPVMException("Error generating csv ", e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("generated");
    }

    @PostMapping(PVMEnpoints.PLAN_UPLOAD)
    public ResponseEntity<List<String>> uploadPlan(@RequestPart MultipartFile file,
                                                   @AuthenticationPrincipal UserPrincipal userPrincipal, String yearUploadPlan) {
        List<String> saved = planService.uploadPlan(file, yearUploadPlan,userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(saved);
    }
}
