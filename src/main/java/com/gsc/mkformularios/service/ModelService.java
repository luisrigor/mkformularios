package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.GoToModelDTO;
import com.gsc.mkformularios.dto.ModelDTO;
import com.gsc.mkformularios.security.UserPrincipal;

public interface ModelService {
    GoToModelDTO goToModel(UserPrincipal userPrincipal, boolean isDetail, Integer idModel, String year);

    Boolean saveModel(UserPrincipal userPrincipal, ModelDTO model, int idModel);
}
