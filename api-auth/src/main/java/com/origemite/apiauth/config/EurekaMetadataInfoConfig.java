package com.origemite.apiauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EurekaMetadataInfoConfig implements InfoContributor {

    private final EurekaInstanceConfigBean eurekaInstanceConfigBean;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> metadata = eurekaInstanceConfigBean.getMetadataMap();
        builder.withDetail("eurekaMetadata", metadata);
    }
}