package com.gsc.mkformularios.config.datasource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DbContext {

    private static ThreadLocal<DbClient> threadLocal;

    public DbContext() {
        threadLocal = new ThreadLocal<>();
    }

    public void setBranchContext(DbClient dataSourceEnum) {
        threadLocal.set(dataSourceEnum);
    }

    public DbClient getBranchContext() {
        return threadLocal.get();
    }

    public static void clearBranchContext() {
        threadLocal.remove();
    }
}
