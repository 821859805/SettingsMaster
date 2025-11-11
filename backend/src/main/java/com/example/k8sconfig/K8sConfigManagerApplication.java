package com.example.k8sconfig;

import com.example.k8sconfig.config.KubernetesClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(KubernetesClientProperties.class)
public class K8sConfigManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sConfigManagerApplication.class, args);
    }
}
