package com.gsc.mkformularios.security;

import com.gsc.mkformularios.constants.AppProfile;

import java.util.Set;

public class UserPrincipal {

    private final String username;
    private final Set<AppProfile> roles;
    private final Long clientId;

    public UserPrincipal(String username, Set<AppProfile> roles, Long clientId) {
        this.username = username;
        this.roles = roles;
        this.clientId = clientId;
    }

    public String getUsername() {
        return username;
    }

    public Set<AppProfile> getRoles() {
        return roles;
    }

    public Long getClientId() {
        return clientId;
    }

    public boolean canUploadFiles() {
        return roles.contains(AppProfile.APPROVAL_MANAGER) || roles.contains(AppProfile.PRODUCT_MANAGER) || roles.contains(AppProfile.UPLOAD_FILE);
    }

    public boolean isManager() {
        return roles.contains(AppProfile.APPROVAL_MANAGER) || roles.contains(AppProfile.PRODUCT_MANAGER);
    }

    public boolean canDownloadCSVFiles() {
        return roles.contains(AppProfile.APPROVAL_MANAGER);
    }

    public boolean canCleanupProjects() {
        return roles.contains(AppProfile.CLEANUP_PROJECTS);
    }

    public boolean canDownloadProjectFiles() {
        return isManager() || roles.contains(AppProfile.DOWNLOAD_PROJECT_FILES);
    }

}
