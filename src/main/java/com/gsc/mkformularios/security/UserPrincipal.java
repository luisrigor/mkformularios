package com.gsc.mkformularios.security;

import com.gsc.mkformularios.constants.AppProfile;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.StringTasks;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserPrincipal {

   private final String username;
   private final Set<AppProfile> roles;
   private final Long clientId;
   private String oidNet;
   private Boolean caMember;
   private String oidDealerParent;

   public UserPrincipal(String username, Set<AppProfile> roles, Long clientId) {
      this.username = username;
      this.roles = roles;
      this.clientId = clientId;
   }

   public UserPrincipal(String username, Set<AppProfile> roles, Long clientId, String oidNet, String oidDealerParent) {
      this.username = username;
      this.roles = roles;
      this.clientId = clientId;
      this.oidNet = oidNet;
      this.oidDealerParent = oidDealerParent;
   }

   public void setOidNet(String oidNet) {
      this.oidNet = oidNet;
   }

   public void setCaMember(Boolean caMember) {
      this.caMember = caMember;
   }
   public void setOidDealerParent(String oidDealerParent) {
      this.oidDealerParent = oidDealerParent;
   }

   public boolean isCAMember() throws SCErrorException {
      if (this.caMember == null && !StringTasks.cleanString(this.getOidNet(), "").equals("") && !StringTasks.cleanString(this.getOidDealerParent(), "").equals("")) {
         Dealer oDealer = null;
         if (this.getOidNet().equalsIgnoreCase("SC00010001")) {
            oDealer = Dealer.getToyotaHelper().getByObjectId(this.getOidDealerParent());
         } else if (this.getOidNet().equalsIgnoreCase("SC00010002")) {
            oDealer = Dealer.getLexusHelper().getByObjectId(this.getOidDealerParent());
         } else if (this.getOidNet().equalsIgnoreCase("SC00010003")) {
            oDealer = Dealer.getCBusHelper().getByObjectId(this.getOidDealerParent());
         }

         if (oDealer != null) {
            this.caMember = new Boolean(oDealer.isCAMember());
         }
      }

      return this.caMember == null ? false : this.caMember;
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
