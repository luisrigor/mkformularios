package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.dto.MapTypesDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PVMCarmodelRepositoryCustom {

    List<MapTypesDTO> getCarTypes();

}
