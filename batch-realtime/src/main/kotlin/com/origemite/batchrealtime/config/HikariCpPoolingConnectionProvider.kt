package com.origemite.batchrealtime.config

import com.jayway.jsonpath.JsonPath
import com.zaxxer.hikari.HikariDataSource
import org.quartz.utils.PoolingConnectionProvider
import org.quartz.utils.PropertiesParser
import org.springframework.beans.factory.config.YamlMapFactoryBean
import org.springframework.core.io.ClassPathResource
import java.sql.Connection
import java.sql.SQLException
import java.util.Properties
import javax.sql.DataSource

class HikariCpPoolingConnectionProvider @Throws(Exception::class) constructor(): PoolingConnectionProvider {

    companion object {
        private const val PROP_JDBC_URL = "jdbcUrl"
        private const val PROP_JDBC_USER = "jdbcUser"
        private const val PROP_JDBC_PASSWORD = "jdbcPassword"
        private const val PROP_JDBC_DRIVER = "jdbcDriver"
        private const val PROP_MAX_POOL_SIZE = "maximumPoolSize"
        private const val PROP_MAX_LIFETIME = "maxLifetime"
        private const val DEFAULT_MAX_POOL_SIZE = 10
        const val PROP_MINIMUM_IDLE = "minimumIdle"
        const val PROP_IDLE_TIMEOUT = "idleTimeout"
        const val CONNECTION_INIT_SQL = "connectionInitSql"
    }

    private var dataSource: HikariDataSource? = null

    init {
        val profiles = System.getProperty("spring.profiles.active") ?: ""
        var env = "local"

        env = when {
            profiles.contains("dev") -> "dev"
            profiles.contains("stg") -> "stg"
            profiles.contains("prd") -> "prd"
            else -> env
        }

        val config = Properties()

        val resource = ClassPathResource("application-$env.yml")
        val yamlFactory = YamlMapFactoryBean().apply {
            setResources(resource)
        }

        var yaml: Map<String, Any>? = yamlFactory.`object`
        yaml = JsonPath.read(yaml, "$.spring.quartz.properties['org.quartz.dataSource'].default")
        println("yaml ------- "+yaml.toString())
        if (yaml != null) {
            config.putAll(yaml)
        }

        val cfg = PropertiesParser(config)
        initialize(cfg)
    }

    private fun initialize(cfg: PropertiesParser) {
        val jdbcUrl = cfg.getStringProperty(PoolingConnectionProvider.DB_URL)
        val jdbcUser = cfg.getStringProperty(PoolingConnectionProvider.DB_USER)
        val jdbcPassword = cfg.getStringProperty(PoolingConnectionProvider.DB_PASSWORD)
        val jdbcDriver = cfg.getStringProperty(PoolingConnectionProvider.DB_DRIVER)
        val maxPoolSize = cfg.getIntProperty(PROP_MAX_POOL_SIZE, DEFAULT_MAX_POOL_SIZE)
        val maxLifetime = cfg.getLongProperty(PROP_MAX_LIFETIME, 141_800_000L)
        val idleTimeout = cfg.getIntProperty(PROP_IDLE_TIMEOUT, 60_000).toLong()
        val minimumIdle = cfg.getIntProperty(PROP_MINIMUM_IDLE, 1)
        val connectionInitSql = cfg.getStringProperty(CONNECTION_INIT_SQL)

        dataSource = HikariDataSource().apply {
            poolName = "QUARTZ_POOL"
            this.jdbcUrl = jdbcUrl
            username = jdbcUser
            password = jdbcPassword
            driverClassName = jdbcDriver
            maximumPoolSize = maxPoolSize
            this.maxLifetime = maxLifetime
            this.idleTimeout = idleTimeout
            this.minimumIdle = minimumIdle
            this.connectionInitSql = connectionInitSql
        }
    }

    @Throws(SQLException::class)
    override fun initialize() {
        // no-op (Quartz 인터페이스 요구사항)
    }

    @Throws(SQLException::class)
    override fun getConnection(): Connection =
        dataSource?.connection ?: throw SQLException("HikariDataSource is not initialized")

    @Throws(SQLException::class)
    override fun shutdown() {
        dataSource?.close()
    }

    override fun getDataSource(): DataSource? = dataSource
}