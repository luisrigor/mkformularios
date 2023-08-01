package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.dto.PVMGetDTO;
import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.ReportDetailRequestDto;
import com.gsc.mkformularios.security.UserPrincipal;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.user.GSCUser;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PVMService {
    PVMGetDTO getPVM(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal);
    PVMDetailDTO getPVMDetail(int idPVM, UserPrincipal userPrincipal);
    Integer newPVM(UserPrincipal userPrincipal, int subDealer);

    void providePVMToDealer(UserPrincipal userPrincipal,String cancelReasons,int idPVM);

    void saveReportDetail(UserPrincipal userPrincipal, List<ReportDetailRequestDto> reportDetailRequestDto, String idPVMS);
    void sendReportDetail(UserPrincipal userPrincipal, List<ReportDetailRequestDto> reportDetailRequestDto, String idPVMS);
    void requestToChange(UserPrincipal userPrincipal, String cancelReasons, String idPVMS);
    void getPVMExcelByMonth(PVMRequestDTO pvmRequestDTO, String pvmMonth, UserPrincipal userPrincipal, HttpServletResponse response);
    String getOidNet(UserPrincipal userPrincipal);
}
