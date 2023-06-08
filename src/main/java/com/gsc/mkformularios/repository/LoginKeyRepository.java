package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.model.toyota.entity.LoginKey;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoginKeyRepository extends JpaRepository<LoginKey, Long> {

    Optional<LoginKey> findFirstByEnabledIsTrue();
}

