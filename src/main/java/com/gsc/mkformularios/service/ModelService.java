package com.gsc.mkformularios.service;

import com.gsc.mkformularios.security.UserPrincipal;

public interface ModelService {
    void goToModel(UserPrincipal userPrincipal, boolean isDetail, int idModel,String year);
}
