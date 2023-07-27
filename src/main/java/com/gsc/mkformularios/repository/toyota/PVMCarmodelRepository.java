package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.dto.MapTypesDTO;
import com.gsc.mkformularios.model.toyota.entity.PVMCarmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PVMCarmodelRepository extends JpaRepository<PVMCarmodel, Integer> {

    @Query(value = "SELECT PC.* FROM PVM_CARMODEL PC " +
            "WHERE PC.active LIKE 'S' " +
            "AND CURRENT DATE BETWEEN PC.DT_FROM AND VALUE( PC.DT_TO, '9999-12-31') ORDER BY TYPE, EXPORT_ORDER, NAME ASC", nativeQuery = true)
    List<PVMCarmodel> getCar();


    @Query(value = "SELECT PC.* FROM PVM_CARMODEL PC WHERE PC.type = :typeModel AND PC.DT_FROM <= :dtFrom " +
            " AND VALUE(DT_TO, '2100-12-31') >=  :dtFrom ORDER BY TYPE, EXPORT_ORDER, NAME",
            nativeQuery = true)
    List<PVMCarmodel> getPVMCarModels(@Param("typeModel") String typeModel, @Param("dtFrom") String dtFrom);

    @Query(value = "SELECT PC.* FROM PVM_CARMODEL PC WHERE PC.DT_FROM <= :dtFrom " +
            " AND VALUE(DT_TO, '2100-12-31') >=  CURRENT DATE ORDER BY TYPE, EXPORT_ORDER, NAME",
            nativeQuery = true)
    List<PVMCarmodel> getPVMCarModels1(@Param("dtFrom") String dtFrom);

}
