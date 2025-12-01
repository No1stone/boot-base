package com.origemite.apiauth.autoconfig;

import com.origemite.apiauth.config.DataSourceConfig;
import com.origemite.apiauth.config.QuerydslConfig;
import com.origemite.lib.common.base.CustomAuditorAware;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@TestConfiguration
@Import({DataSourceConfig.class, QuerydslConfig.class, CustomAuditorAware.class})
@ImportAutoConfiguration({
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@EnableJpaAuditing
public class H2Config {
}
