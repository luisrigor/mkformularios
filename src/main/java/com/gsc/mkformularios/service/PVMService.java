package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.security.UserPrincipal;

import javax.servlet.http.HttpServletResponse;

public interface PVMService {
    PVMDetailDTO getPVM(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal);
    PVMDetailDTO getPVMDetail(int idPVM, UserPrincipal userPrincipal);
    void getPVMExcelByMonth(PVMRequestDTO pvmRequestDTO, String pvmMonth, UserPrincipal userPrincipal, HttpServletResponse response);
}
