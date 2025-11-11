package com.k8smaster.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kubernetes.client")
public class KubernetesClientProperties {

    /**
     * kubeconfig 文件路径，默认使用集群内或本地默认配置。
     */
    private String configFile;

    /**
     * 默认命名空间。
     */
    private String namespace = "default";

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
