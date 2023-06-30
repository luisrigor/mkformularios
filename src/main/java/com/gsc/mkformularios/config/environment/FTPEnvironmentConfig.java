package com.gsc.mkformularios.config.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.ftp")
public class FTPEnvironmentConfig {
   private String domain;
   private String username;
   private String password;
   private String folderHomo;
   private String folderAs400;

   public FTPEnvironmentConfig() {
   }

   public String getDomain() {
      return domain;
   }

   public void setDomain(String domain) {
      this.domain = domain;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getFolderHomo() {
      return folderHomo;
   }

   public void setFolderHomo(String folderHomo) {
      this.folderHomo = folderHomo;
   }

   public String getFolderAs400() {
      return folderAs400;
   }

   public void setFolderAs400(String folderAs400) {
      this.folderAs400 = folderAs400;
   }
}
