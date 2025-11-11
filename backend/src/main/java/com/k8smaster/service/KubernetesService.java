package com.k8smaster.service;

import com.k8smaster.domain.dto.K8sResourceSummary;

import java.util.List;

public interface KubernetesService {
    List<K8sResourceSummary> listDeployments(String namespace);

    List<K8sResourceSummary> listPods(String namespace);

    String getResourceYaml(String namespace, String kind, String name);

    void createResource(String namespace, String yamlContent);

    void updateResource(String namespace, String kind, String name, String yamlContent);

    void deleteResource(String namespace, String kind, String name);
}
