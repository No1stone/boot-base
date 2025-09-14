package com.origemite.apiauth.config;


import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManager",
		transactionManagerRef = "transactionManager",
		basePackages = {"com.origemite.apiauth","com.origemite.lib.**"}
)
public class DataSourceConfig {

	@Primary
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix="spring.datasource.default")
	public DataSource dataSource() {
		return  DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Primary
	@Bean(name = "entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    		EntityManagerFactoryBuilder entityManagerFactoryBuilder,
    		@Qualifier("dataSource") DataSource dataSource) {
        return entityManagerFactoryBuilder.dataSource(dataSource)
        		.packages("com.origemite.apiauth","com.origemite.lib.**")
				.persistenceUnit("PERSISTENCE_DEFAULT")
        		.build();
    }

	@Primary
	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManager") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
