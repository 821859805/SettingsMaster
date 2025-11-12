package com.k8smaster.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kubernetes.client")
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

    public String getMasterUrl() {
        return masterUrl;
    }

    public void setMasterUrl(String masterUrl) {
        this.masterUrl = masterUrl;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Boolean getTrustCerts() {
        return trustCerts;
    }

    public void setTrustCerts(Boolean trustCerts) {
        this.trustCerts = trustCerts;
    }
}
