package com.k8smaster.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kubernetes.client")
@Data
public class KubernetesClientProperties {

    /**
     * Kubernetes APIServer 地址。
     */
    private String masterUrl;

    /**
     * OAuth Token。
     */
    private String oauthToken;

    /**
     * 基本认证用户名。
     */
    private String username;

    /**
     * 基本认证密码。
     */
    private String password;

    /**
     * 默认命名空间。
     */
    private String namespace = "default";

    /**
     * 是否信任非受信证书。
     */
    private Boolean trustCerts = Boolean.TRUE;
}
