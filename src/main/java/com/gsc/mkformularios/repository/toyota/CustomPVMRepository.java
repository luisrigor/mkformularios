package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.dto.PVMRequestDTO;
import com.gsc.mkformularios.dto.SalesPlates;
import com.gsc.mkformularios.model.toyota.entity.PVMMonthlyReport;
import com.gsc.mkformularios.security.UserPrincipal;

import java.util.List;

public interface CustomPVMRepository {

    List<SalesPlates> getSalesAndPlates(int idPVM);
    List<PVMMonthlyReport> getPVM(PVMRequestDTO pvmRequestDTO, UserPrincipal userPrincipal);

}
