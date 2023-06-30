package com.gsc.mkformularios.constants;

public enum ProjectFileStatus {
    /**
     * The file has errors and could not be parsed. *
     */
    ERROR,
    /**
     * The file was just received.
     */
    DRAFT,
    /**
     * The file has been saved.
     */
    PENDING,
    /**
     * The file was processed without errors.
     */
    PROCESSED
}
