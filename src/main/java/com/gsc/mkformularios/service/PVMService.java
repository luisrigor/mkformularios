package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PVMDetailDTO;
import com.gsc.mkformularios.security.UserPrincipal;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.user.GSCUser;

public interface PVMService {
    PVMDetailDTO getPVMDetail(int idPVM, UserPrincipal userPrincipal);
    Boolean newPVM(GSCUser oGSCUser, int subDealer);
}
