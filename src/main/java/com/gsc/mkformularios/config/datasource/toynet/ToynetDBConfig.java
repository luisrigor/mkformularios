package com.gsc.mkformularios.config.datasource.toynet;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Profile(value = {"development","staging","production"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "toyEntityManagerFactory",
        basePackages = {"com.gsc.mkformularios.repository.toynet"}
)
public class ToynetDBConfig {

    @Autowired
    private Environment env;

    @Value("${sc.config.file}")
    private String scConfigFile;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @Value("${app.datasource.third.jndi}")
    private String jndi;

    @PostConstruct
    private void init() {
//        SCGlobalPreferences.setResources(scConfigFile);
//        ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
//        DB2SimpleDataSource dbToynet = new DB2SimpleDataSource();
//        dbToynet.setServerName("scdbesrva.sc.pt");
//        dbToynet.setPortNumber(50000);
//        dbToynet.setDatabaseName("DBTOYNET");
//        dbToynet.setDriverType(4);
//        dbToynet.setUser("db2inst1");
//        dbToynet.setPassword("db2admin");
//        conn.setDataSource(dbToynet, "jdbc/dbtoynet");
//        log.info("Datasource initialized successfully: jdbc/dbtoynet");
//        DB2SimpleDataSource usrLogon = new DB2SimpleDataSource();
//        usrLogon.setServerName("scdbesrvb.sc.pt");
//        usrLogon.setPortNumber(50000);
//        usrLogon.setDatabaseName("USRLOGON");
//        usrLogon.setDriverType(4);
//        usrLogon.setUser("db2inst1");
//        usrLogon.setPassword("db2admin");
//        conn.setDataSource(usrLogon, "jdbc/usrlogon");
//        log.info("Datasource initialized successfully: jdbc/usrlogon");
    }

    @Bean(name="toyDataSource")
    @ConfigurationProperties(prefix = "toyn.datasource")
    DataSource dataSource() throws NamingException {
        log.info("INIT PRIMARY JNDI: " + jndi);
        return (DataSource) new InitialContext().lookup(jndi);
    }

    @Bean(name = "toyEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean toyEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("toyDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.mkformularios.model.toynet.entity")
                .persistenceUnit("toyPersistenceUnit")
                .properties(getHibernateProperties())
                .build();
    }

    @Bean(name = "toyTransactionManager")
    PlatformTransactionManager toyTransactionManager(@Qualifier("toyEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", hibernateDialect);
        return hibernateProperties;
    }

}
