package com.gsc.mkformularios.config.datasource.toyota;

import com.ibm.db2.jcc.DB2SimpleDataSource;
import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.initialization.SCGlobalPreferences;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        entityManagerFactoryRef = "msEntityManagerFactory",
        transactionManagerRef = "msTransactionManager",
        basePackages = {"com.gsc.mkformularios.repository.toyota"}
)
@Log4j
@RequiredArgsConstructor
@DependsOn("dataSourceRoutingLocal")
public class DbConfigLocal {

    private final DataSourceRoutingLocal dataSourceRoutingLocal;
    private final String USER = "db2inst1";
    private final String PASSWORD = "db2admin";

    @Autowired
    private  Environment env;

    @Value("${sc.config.file}")
    private String scConfigFile;

    @PostConstruct
    private void init() {
        SCGlobalPreferences.setResources(scConfigFile);
        ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
        DB2SimpleDataSource dbToynet = new DB2SimpleDataSource();
        dbToynet.setServerName("scdbesrva.sc.pt");
        dbToynet.setPortNumber(50000);
        dbToynet.setDatabaseName("DBTOYNET");
        dbToynet.setDriverType(4);
        dbToynet.setUser(USER);
        dbToynet.setPassword(PASSWORD);
        conn.setDataSource(dbToynet, "jdbc/dbtoynet");
        log.info("Datasource initialized successfully: jdbc/dbtoynet");
        DB2SimpleDataSource dblexxtaps = new DB2SimpleDataSource();
        dblexxtaps.setServerName("scdbesrvb.sc.pt");
        dblexxtaps.setPortNumber(50000);
        dblexxtaps.setDatabaseName("USRLOGON");
        dblexxtaps.setDriverType(4);
        dblexxtaps.setUser(USER);
        dblexxtaps.setPassword(PASSWORD);
        conn.setDataSource(dblexxtaps, "jdbc/lexxtaps");
        log.info("Datasource initialized successfully: jdbc/lexxtaps");
        DB2SimpleDataSource usrLogon = new DB2SimpleDataSource();
        usrLogon.setServerName("scdbesrvb.sc.pt");
        usrLogon.setPortNumber(50000);
        usrLogon.setDatabaseName("USRLOGON");
        usrLogon.setDriverType(4);
        usrLogon.setUser(USER);
        usrLogon.setPassword(PASSWORD);
        conn.setDataSource(usrLogon, "jdbc/usrlogon");
        log.info("Datasource initialized successfully: jdbc/usrlogon");
    }

    @Primary
    @Bean(name="msDatasource")
    DataSource dataSource(){
        return dataSourceRoutingLocal;
    }

    @Primary
    @Bean(name = "msEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("msDatasource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.mkformularios.model.toyota.entity")
                .persistenceUnit("msPersistenceUnit")
                .properties(getHibernateProperties())
                .build();
    }

    @Primary
    @Bean(name = "msTransactionManager")
    PlatformTransactionManager transactionManager(@Qualifier("msEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Map<String, Object> getHibernateProperties() {
        Map<String, Object> hibernateProperties = new HashMap<>();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.DB2Dialect");
        return hibernateProperties;
    }


}
