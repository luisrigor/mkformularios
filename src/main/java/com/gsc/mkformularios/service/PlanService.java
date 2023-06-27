package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.PlanDTO;
import com.gsc.mkformularios.security.UserPrincipal;

public interface PlanService {
    PlanDTO goToEditPlan(UserPrincipal userPrincipal, String year);
}
