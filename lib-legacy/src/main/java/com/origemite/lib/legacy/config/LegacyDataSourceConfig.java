package com.origemite.lib.legacy.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "EntityManager_LEGACY",
		transactionManagerRef = "TransactionManager_LEGACY",
		basePackages = {"com.origemite.lib.legacy"}
)
public class LegacyDataSourceConfig {

	@Bean(name = "dataSource_LEGACY")
	@ConfigurationProperties(prefix="spring.datasource.legacy")
	public DataSource dataSource() {
		return  DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean(name = "EntityManager_LEGACY")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    		EntityManagerFactoryBuilder builder,
    		@Qualifier("dataSource_LEGACY") DataSource dataSource) {
        return builder.dataSource(dataSource)
        		.packages("com.origemite.lib.legacy")
				.persistenceUnit("PERSISTENCE_LEGACY")
        		.build();
    }

	@Bean(name = "TransactionManager_LEGACY")
	public PlatformTransactionManager transactionManager(
			@Qualifier("EntityManager_LEGACY") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
