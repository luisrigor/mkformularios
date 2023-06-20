package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.security.UserPrincipal;

public interface PVMService {
    PVMDetailDTO getPVMDetail(int idPVM, UserPrincipal userPrincipal);
}
