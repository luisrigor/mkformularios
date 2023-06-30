package com.gsc.mkformularios.service;

import com.gsc.mkformularios.dto.BudgetDTO;
import com.gsc.mkformularios.security.UserPrincipal;

public interface BudgetService {

    BudgetDTO editBudget(String yearSelect, UserPrincipal userPrincipal);
}
