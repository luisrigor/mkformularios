package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PlanService {
    PlanDTO goToEditPlan(UserPrincipal userPrincipal, String year);
    List<String> uploadPlan(MultipartFile file, String yearPlanUpload, UserPrincipal userPrincipal);
    List<String[]> downloadPlan(String yearPlan, UserPrincipal userPrincipal, HttpServletResponse response);
}
