package com.gsc.mkformularios.repository.toyota;

import com.gsc.mkformularios.model.toyota.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    String LOGIN_ENABLED = "loginEnabled";
    String KEY_CREATION = "keyCreationStatus";
    String TOKEN_EXPIRATION_MILISECONDS = "tokenExpirationMiliseconds";

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
