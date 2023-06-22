package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.GoToModelDTO;
import com.gsc.mkformularios.dto.ModelDTO;
import com.gsc.mkformularios.security.UserPrincipal;

public interface ModelService {
    GoToModelDTO goToModel(UserPrincipal userPrincipal, boolean isDetail, int idModel, String year);

    void saveModel(UserPrincipal userPrincipal, ModelDTO model, int idModel);
}
