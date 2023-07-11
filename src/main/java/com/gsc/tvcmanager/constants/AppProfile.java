package com.gsc.tvcmanager.constants;

import lombok.Getter;

@Getter
public enum AppProfile {

    /**
     * Gestor de Homologação.
     */
    TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP(40, 10191),
    /**
     * Gestor de Produto.
     */
    TVC_MANAGER_PRF_TOYOTA_LEXUS_CONC(41, 10192),
    /**
     * Can upload files.
     */
    TVC_MANAGER_PRF_TOYOTA_LEXUS_INST(42, 000),
    TVC_MANAGER_ROLE_EDIT_REGISTER(-1, -2),
    TVC_MANAGER_ROLE_DEALER_FILTER(-1, -2),
    TVC_MANAGER_ROLE_ACTIVE_DEALERS(-1, -2),
    TVC_MANAGER_ROLE_OPEN_BUTTON(-1, -2),
    TVC_MANAGER_ROLE_NEW_RECORD(-1, -2),
    TVC_MANAGER_ROLE_SAVE_BUTTONS(-1, -2);
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

    AppProfile(Integer idToyota, Integer idLexus) {
        this.idToyota = idToyota;
        this.idLexus = idLexus;
    }
}
