package com.gsc.mkformularios.config.datasource.toynet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Profile(value = {"local"} )
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "toyEntityManagerFactory",
//        transactionManagerRef = "toyTransactionManager",
        basePackages = {"com.gsc.mkformularios.repository.toynet"}
)
public class ToynetDBConfigLocal {


//    private static final Logger log = LoggerFactory.getLogger(DbConfigLocal.class);



    @Autowired
    private Environment env;

    @Value("${sc.config.file}")
    private String scConfigFile;

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
    @ConfigurationProperties(prefix = "toy.datasource")
    DataSource dataSource(){
        return DataSourceBuilder.create()
                .url(env.getProperty("app.datasource.third.url"))
                .driverClassName(env.getProperty("app.datasource.third.driver-class-name"))
                .username(env.getProperty("app.datasource.third.username"))
                .password(env.getProperty("app.datasource.third.password"))
                .build();
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
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        return hibernateProperties;
    }

}
