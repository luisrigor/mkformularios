package com.gsc.mkformularios.repository.toynet;

import com.gsc.mkformularios.dto.RetailDealerDTO;
import com.gsc.mkformularios.model.toynet.entity.ToyotaRetailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToyotaRetailerRepository extends JpaRepository<ToyotaRetailer, String>, CustomRetailerRepository {

    @Query("SELECT NEW com.gsc.mkformularios.dto.RetailDealerDTO(LR.objectid, LR.desig, LR.dealerCode)  " +
            " FROM ToyotaRetailer LR ORDER BY LR.desig")
    List<RetailDealerDTO> findAllOrderByDesig();

    @Query("SELECT NEW com.gsc.mkformularios.dto.RetailDealerDTO(LR.objectid, LR.desig, LR.dealerCode) FROM ToyotaRetailer LR " +
            " WHERE LR.isCaMember = 'S' ORDER BY LR.desig")
    List<RetailDealerDTO> findAllOrderByDesigCaMember();

    @Query("SELECT TR.objectid FROM ToyotaRetailer TR WHERE TR.objectid <> :objectId")
    List<String> findAllByObjectid(@Param("objectId") String objectId);

    List<ToyotaRetailer> findAllByObjectidNotIn(List<String> objectId);
}
