package com.gsc.tvcmanager.config;

import com.ibm.db2.jcc.DB2SimpleDataSource;
import com.sc.commons.dbconnection.ServerJDBCConnection;
import com.sc.commons.initialization.SCGlobalPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
        entityManagerFactoryRef = "msEntityManagerFactory",
        transactionManagerRef = "msTransactionManager",
        basePackages = {"com.gsc.tvcmanager.repository.toyota"}
)
public class DbConfigLocal {

    private static final Logger log = LoggerFactory.getLogger(DbConfigLocal.class);

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
        dbToynet.setUser("db2inst1");
        dbToynet.setPassword("db2admin");
        conn.setDataSource(dbToynet, "jdbc/dbtoynet");
        log.info("Datasource initialized successfully: jdbc/dbtoynet");
        DB2SimpleDataSource dblexxtaps = new DB2SimpleDataSource();
        dblexxtaps.setServerName("scdbesrvb.sc.pt");
        dblexxtaps.setPortNumber(50000);
        dblexxtaps.setDatabaseName("USRLOGON");
        dblexxtaps.setDriverType(4);
        dblexxtaps.setUser("db2inst1");
        dblexxtaps.setPassword("db2admin");
        conn.setDataSource(dblexxtaps, "jdbc/lexxtaps");
        log.info("Datasource initialized successfully: jdbc/lexxtaps");
    }

    @Primary
    @Bean(name="msDatasource")
    DataSource dataSource(){
        return DataSourceBuilder.create()
                .url(env.getProperty("spring.datasource.url"))
                .driverClassName(env.getProperty("spring.datasource.driver-class-name"))
                .username(env.getProperty("spring.datasource.username"))
                .password(env.getProperty("spring.datasource.password"))
                .build();
    }

    @Primary
    @Bean(name = "msEntityManagerFactory")
    LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("msDatasource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.gsc.tvcmanager.model.toyota.entity")
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
