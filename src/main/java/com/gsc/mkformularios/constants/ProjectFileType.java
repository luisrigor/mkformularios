package com.gsc.mkformularios.constants;

public enum ProjectFileType {

    BASEFILE("application/vnd.ms-excel"),
    WVTA("application/pdf"),
    SUFFIXES("application/rtf"),
    IMT("text/csv"),
    TXT("text/plain"),
    FAM("application/pdf"),
    FAM_FIXED("image/png");

    private final String mimeType;

    private ProjectFileType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

}

