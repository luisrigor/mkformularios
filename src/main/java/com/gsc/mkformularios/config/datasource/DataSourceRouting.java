package com.gsc.mkformularios.config.datasource;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile(value = {"development","staging","production"})
public class DataSourceRouting extends AbstractRoutingDataSource {


    private DbOneConfig dataSourceOneConfig;
    private DBTwoConfig dataSourceTwoConfig;
    private DbContext dataSourceContextHolder;

    public DataSourceRouting(DbContext dataSourceContextHolder, DbOneConfig dataSourceOneConfig,
                                  DBTwoConfig dataSourceTwoConfig) {
        this.dataSourceOneConfig = dataSourceOneConfig;
        this.dataSourceTwoConfig = dataSourceTwoConfig;
        this.dataSourceContextHolder = dataSourceContextHolder;

        Map<Object, Object> dataSourceMap = new HashMap<>();
        try {
            dataSourceMap.put(DbClient.DB_TOYOTA, dataSourceOneDataSource());
            dataSourceMap.put(DbClient.DB_LEXUS, dataSourceTwoDataSource());
            this.setTargetDataSources(dataSourceMap);
            this.setDefaultTargetDataSource(dataSourceOneDataSource());
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceContextHolder.getBranchContext();
    }

    public DataSource dataSourceOneDataSource() throws NamingException {
        return (DataSource) new InitialContext().lookup(dataSourceOneConfig.getJndi());
    }

    public DataSource dataSourceTwoDataSource() throws NamingException {
        return (DataSource) new InitialContext().lookup(dataSourceTwoConfig.getJndi());
    }
}
