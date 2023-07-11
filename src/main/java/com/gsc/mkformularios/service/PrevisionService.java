package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PrevisionDTO;
import com.gsc.mkformularios.security.UserPrincipal;

public interface PrevisionService{

    PrevisionDTO getUsedCarsAllPrevisionSalesMonth(UserPrincipal userPrincipal);
}
