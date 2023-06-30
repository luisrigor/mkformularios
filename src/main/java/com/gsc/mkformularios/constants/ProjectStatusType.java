package com.gsc.mkformularios.constants;

public enum ProjectStatusType {
    REFRESHING, // quando tiver a fazer update do projecto
    ERROR, // quando da erro a processar o basefile
    DRAFT, // quando esta ainda a processar o basefile para criação de projeto
    PENDING, // So tem o basefile e falta o upload resto dos ficheiros
    REGISTERED, // quando fizerem o upload dos 2 ficheiros necessários (wvta, dencodingSuffix)
    VALIDATED, // sufixos validados
    SUBMITTED, // quando receber o imt
    DONE // Quando receber fams e txt
}
