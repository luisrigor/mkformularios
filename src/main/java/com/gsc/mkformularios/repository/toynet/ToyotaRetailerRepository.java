package com.gsc.mkformularios.repository.toynet;

import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.gsc.mkformularios.model.toynet.entity.ToyotaRetailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ToyotaRetailerRepository extends JpaRepository<ToyotaRetailer, String> {

    @Query("SELECT NEW com.gsc.mkformularios.dto.RetailDealerDTO(LR.objectid, LR.desig, LR.dealerCode)  " +
            " FROM ToyotaRetailer LR ORDER BY LR.desig")
    List<RetailDealerDTO> findAllOrderByDesig();

    @Query("SELECT NEW com.gsc.mkformularios.dto.RetailDealerDTO(LR.objectid, LR.desig, LR.dealerCode) FROM ToyotaRetailer LR " +
            " WHERE LR.isCaMember = 'S' ORDER BY LR.desig")
    List<RetailDealerDTO> findAllOrderByDesigCaMember();
}
