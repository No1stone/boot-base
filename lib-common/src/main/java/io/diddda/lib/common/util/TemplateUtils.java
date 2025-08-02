package io.diddda.lib.common.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Component
@Slf4j
public class TemplateUtils {

    private static ApplicationContext applicationContext;
    private static SpringTemplateEngine templateEngine;
    private static ThymeleafViewResolver viewResolver;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext
            , ThymeleafViewResolver viewResolver
    ) {
        TemplateUtils.applicationContext = applicationContext;
        TemplateUtils.viewResolver = viewResolver;
    }

    @PostConstruct
    public void init() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setCacheable(false);

        templateEngine = new SpringTemplateEngine();
        templateEngine.setEnableSpringELCompiler(true); // Spring EL 사용
        templateEngine.setTemplateResolver(templateResolver);

        viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");

    }
}
