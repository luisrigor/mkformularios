package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.dto.BudgetUploadDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMBudget;
import com.gsc.mkformularios.security.UserPrincipal;
import com.opencsv.bean.StatefulBeanToCsv;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BudgetService {

    BudgetDTO editBudget(String yearSelect, UserPrincipal userPrincipal);
    void saveBudget(String yearSelect, UserPrincipal userPrincipal, List<PVMBudget> lstPVMBudget);
    List<String[]> downloadBudget(String yearBudget, UserPrincipal userPrincipal, HttpServletResponse response);
    void uploadBudget(String yearBudget, UserPrincipal userPrincipal, MultipartFile file);
}
