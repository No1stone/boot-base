package ai.mintit.drp.lib.legacy.config;

import com.origemite.lib.common.factory.YamlPropertySourceFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-legacy-test.yml", factory = YamlPropertySourceFactory.class)
@ContextConfiguration(classes = TestConfig.class)
@EnableConfigurationProperties
public @interface ClientTest {}