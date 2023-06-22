package com.gsc.mkformularios.service;

import com.gsc.mkformularios.security.UserPrincipal;

public interface PlanService {

    void goToEditPlan(UserPrincipal userPrincipal, int year);
}
