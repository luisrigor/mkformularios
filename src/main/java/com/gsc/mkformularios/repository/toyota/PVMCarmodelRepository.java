package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.dto.MapTypesDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PVMCarmodelRepository extends JpaRepository<PVMCarmodel, Integer>, PVMCarmodelRepositoryCustom {

    @Query(value = "SELECT PC.* FROM PVM_CARMODEL PC " +
            "WHERE PC.active LIKE 'S' " +
            "AND CURRENT DATE BETWEEN PC.DT_FROM AND VALUE( PC.DT_TO, '9999-12-31') ORDER BY TYPE, EXPORT_ORDER, NAME ASC", nativeQuery = true)
    List<PVMCarmodel> getCar();

}
