package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.model.entity.PVMCarmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PVMCarmodelRepository extends JpaRepository<PVMCarmodel, Integer> {

    @Query(value = "SELECT PC.* FROM PVM_CARMODEL PC " +
            "WHERE PC.active LIKE 'S' " +
            "AND CURRENT DATE BETWEEN PC.DT_FROM AND VALUE( PC.DT_TO, '9999-12-31') ", nativeQuery = true)
    List<PVMCarmodel> getCar();
}
