package com.k8smaster;

import com.k8smaster.config.KubernetesClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableConfigurationProperties(KubernetesClientProperties.class)
@MapperScan("com.k8smaster.mapper")
public class K8sMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(K8sMasterApplication.class, args);
    }
}
