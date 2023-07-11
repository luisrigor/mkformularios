package com.gsc.mkformularios.config.datasource.toyota;

import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.initialization.SCGlobalPreferences;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
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
import java.util.List;
import java.util.Map;

@Profile(value = {"development","staging","production"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "msEntityManagerFactory",
        transactionManagerRef = "msTransactionManager",
        basePackages = {"com.gsc.mkformularios.repository.toyota"}
)
@RequiredArgsConstructor
@DependsOn("dataSourceRouting")
public class DbConfig {

    private final DataSourceRouting dataSourceRouting;

    private static final Logger log = LoggerFactory.getLogger(DbConfig.class);

    @Value("${secundary.dbs.jndi}")
    private List<String> jndis;

    @Value("${sc.config.file}")
    private String scConfigFile;

    @Value("${spring.datasource.jndi-name}")
    private String jndi;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;

    @PostConstruct
    private void init() {
        SCGlobalPreferences.setResources(scConfigFile);
        jndis.stream().forEach(jndiName -> {
            try {
                InitialContext ctx = new InitialContext();
                ServerJDBCConnection conn = ServerJDBCConnection.getInstance();
                conn.setDataSource((DataSource) ctx.lookup(jndiName), jndiName);
                log.info("Datasource initialized successfully: {}", jndiName);
            } catch (NamingException e) {
                log.error("Error initializing datasource ({}): {}", jndiName, e.getMessage());
            }
        });
    }
    @Primary
    @Bean(name="msDatasource",
    destroyMethod = "")
    DataSource dataSource() {
        return dataSourceRouting;
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
        hibernateProperties.put("hibernate.dialect", hibernateDialect);
        return hibernateProperties;
    }

}
