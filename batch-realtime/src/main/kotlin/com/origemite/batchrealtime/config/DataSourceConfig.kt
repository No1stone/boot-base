package com.origemite.batchrealtime.config

import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.EntityManagerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "entityManager",
    transactionManagerRef = "transactionManager",
    basePackages = ["com.origemite.batchrealtime.**", "com.origemite.lib.**"]
)
class DataSourceConfig {

    @Primary
    @Bean(name = ["dataSource"])
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    fun dataSource(): DataSource
    = DataSourceBuilder.create().type(HikariDataSource::class.java).build()

    @Primary
    @Bean(name = ["entityManager"])
    fun entityManager(builder: org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
                      , @Qualifier("dataSource")dataSource: DataSource ): LocalContainerEntityManagerFactoryBean =
        builder
            .dataSource(dataSource)
            .packages("com.origemite.batchrealtime.**", "com.origemite.lib.**")
            .persistenceUnit("PERSISTENCE_BATCH")
            .build()


    @Primary
    @Bean(name = ["transactionManager"])
    fun transactionManager(
        @Qualifier("entityManager") entityManagerFactory: EntityManagerFactory
    ): PlatformTransactionManager =
        JpaTransactionManager(entityManagerFactory)
}
