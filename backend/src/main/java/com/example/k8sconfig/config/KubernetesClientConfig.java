package com.example.k8sconfig.config;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class KubernetesClientConfig {

    private static final Logger log = LoggerFactory.getLogger(KubernetesClientConfig.class);

    @Bean
    public KubernetesClient kubernetesClient(KubernetesClientProperties properties) {
        Config config = configure(properties);
        return new KubernetesClientBuilder().withConfig(config).build();
    }

    private Config configure(KubernetesClientProperties properties) {
        String configFile = properties.getConfigFile();
        if (StringUtils.hasText(configFile)) {
            Path path = Path.of(configFile);
            if (Files.exists(path)) {
                try {
                    String kubeConfig = Files.readString(path);
                    log.info("使用 kubeconfig 文件创建 Kubernetes 客户端: {}", configFile);
                    return Config.fromKubeconfig(kubeConfig);
                } catch (IOException ex) {
                    throw new IllegalStateException("读取 kubeconfig 文件失败: " + configFile, ex);
                }
            }
            log.warn("kubeconfig 文件不存在: {}，将尝试自动配置", configFile);
        }
        log.info("使用默认配置创建 Kubernetes 客户端");
        return Config.autoConfigure(null);
    }
}
