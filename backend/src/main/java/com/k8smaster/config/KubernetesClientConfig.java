package com.k8smaster.config;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class KubernetesClientConfig {

    private static final Logger log = LoggerFactory.getLogger(KubernetesClientConfig.class);

    @Bean
    public KubernetesClient kubernetesClient(KubernetesClientProperties properties) {
        Config config = configure(properties);
        return new KubernetesClientBuilder().withConfig(config).build();
    }

    private Config configure(KubernetesClientProperties properties) {
        if (StringUtils.hasText(properties.getMasterUrl())) {
            ConfigBuilder builder = new ConfigBuilder()
                    .withMasterUrl(properties.getMasterUrl())
                    .withNamespace(StringUtils.hasText(properties.getNamespace()) ? properties.getNamespace() : "default");
            if (StringUtils.hasText(properties.getOauthToken())) {
                builder.withOauthToken(properties.getOauthToken());
            }
            if (StringUtils.hasText(properties.getUsername())) {
                builder.withUsername(properties.getUsername());
            }
            if (StringUtils.hasText(properties.getPassword())) {
                builder.withPassword(properties.getPassword());
            }
            if (properties.getTrustCerts() != null) {
                builder.withTrustCerts(properties.getTrustCerts());
            }
            log.info("使用属性配置创建 Kubernetes 客户端: masterUrl={}", properties.getMasterUrl());
            return builder.build();
        }
        log.warn("未配置 kubernetes.client.master-url，回退到自动配置。");
        Config config = Config.autoConfigure(null);
        if (StringUtils.hasText(properties.getNamespace())) {
            config.setNamespace(properties.getNamespace());
        }
        if (properties.getTrustCerts() != null) {
            config.setTrustCerts(properties.getTrustCerts());
        }
        return config;
    }
}
