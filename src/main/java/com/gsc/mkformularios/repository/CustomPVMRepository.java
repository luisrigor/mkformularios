package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.dto.SalesPlates;

import java.util.List;

public interface CustomPVMRepository {

    List<SalesPlates> getSalesAndPlates(int idPVM);
}
