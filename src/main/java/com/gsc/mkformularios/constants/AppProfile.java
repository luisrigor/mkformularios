package com.gsc.mkformularios.constants;

public enum AppProfile {

    /**
     * Gestor de Homologação.
     */
    APPROVAL_MANAGER(535),
    /**
     * Gestor de Produto.
     */
    PRODUCT_MANAGER(534),
    /**
     * Can upload files.
     */
    UPLOAD_FILE(-1),
    /**
     * Can cleanup projects.
     */
    CLEANUP_PROJECTS(-1),
    /**
     * Can download project files.
     */
    DOWNLOAD_PROJECT_FILES(-1);

    private final Integer id;

    AppProfile(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
