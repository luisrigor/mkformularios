package com.gsc.mkformularios.repository.toynet;

import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.gsc.mkformularios.model.toynet.entity.LexusRetailer;
import com.gsc.mkformularios.model.toynet.entity.ToyotaRetailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LexusRetailerRepository extends JpaRepository<LexusRetailer, String> {

    @Query("SELECT LR.objectId, LR.desig, LR.dealerCode FROM LexusRetailer LR ORDER BY LR.desig")
    List<RetailDealerDTO> findAllOrderByDesig();

    List<LexusRetailer> findAllByObjectIdNotIn(List<String> objectId);
}
