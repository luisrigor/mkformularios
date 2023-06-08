package com.gsc.mkformularios.repository;

import com.gsc.mkformularios.model.toyota.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    final String LOGIN_ENABLED = "loginEnabled";
    final String KEY_CREATION = "keyCreationStatus";
    final String TOKEN_EXPIRATION_MILISECONDS = "tokenExpirationMiliseconds";

    Configuration findByName(String name);

    default Boolean isLoginEnabled() {
        return Boolean.valueOf(findByName(LOGIN_ENABLED).getValue());
    }

    default Boolean isKeyCreationEnabled() {
        return Boolean.valueOf(findByName(KEY_CREATION).getValue());
    }

    default Long getTokenExpirationMsec() {
        return Long.valueOf(findByName(TOKEN_EXPIRATION_MILISECONDS).getValue());
    }
}
