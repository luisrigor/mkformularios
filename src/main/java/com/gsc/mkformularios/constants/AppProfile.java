package com.gsc.mkformularios.constants;

import lombok.Getter;

import static com.gsc.mkformularios.constants.DATAConstants.*;

@Getter
public enum AppProfile {

    /**
     * Gestor de Homologação.
     */
    DEALER(ID_PRF_TOYOTA_DEALER, ID_PRF_LEXUS_DEALER),
    /**
     * Gestor de Produto.
     */
    TCAP(ID_PRF_TOYOTA_TCAP, ID_PRF_LEXUS_TCAP),
    /**
     * Can upload files.
     */
    CA(ID_PRF_TOYOTA_CA, -1);
    /**
     * Can cleanup projects.
     */
//    CLEANUP_PROJECTS(-1),
    /**
     * Can download project files.
     */
//    DOWNLOAD_PROJECT_FILES(-1);

    private final Integer idToyota;
    private final Integer idLexus;

//    AppProfile(Integer id) {
//        this.idToyota = id;
//    }

    AppProfile(Integer idToyota, Integer idLexus) {
        this.idToyota = idToyota;
        this.idLexus = idLexus;
    }
}
