package com.gsc.mkformularios.config.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.email")
public class MailEnvironmentConfig {

   private String famSubject;
   private String mappingSubject;
   private List<String> bcc;
   private MailBrand toyota;
   private MailBrand lexus;

   public String getFamSubject() {
      return famSubject;
   }

   public void setFamSubject(String famSubject) {
      this.famSubject = famSubject;
   }

   public String getMappingSubject() {
      return mappingSubject;
   }

   public void setMappingSubject(String mappingSubject) {
      this.mappingSubject = mappingSubject;
   }

   public List<String> getBcc() {
      return bcc;
   }

   public void setBcc(List<String> bcc) {
      this.bcc = bcc;
   }

   public MailBrand getToyota() {
      return toyota;
   }

   public void setToyota(MailBrand toyota) {
      this.toyota = toyota;
   }

   public MailBrand getLexus() {
      return lexus;
   }

   public void setLexus(MailBrand lexus) {
      this.lexus = lexus;
   }

   public static class MailBrand {

      private List<String> to;
      private List<String> cc;

      public List<String> getTo() {
         return to;
      }

      public void setTo(List<String> to) {
         this.to = to;
      }

      public List<String> getCc() {
         return cc;
      }

      public void setCc(List<String> cc) {
         this.cc = cc;
      }
   }
}
