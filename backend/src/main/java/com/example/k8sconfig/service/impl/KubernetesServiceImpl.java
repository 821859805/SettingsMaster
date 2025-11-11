package com.example.k8sconfig.service.impl;

import com.example.k8sconfig.dto.K8sResourceSummary;
import com.example.k8sconfig.service.KubernetesService;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.StatefulSet;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.utils.Serialization;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KubernetesServiceImpl implements KubernetesService {

    private final KubernetesClient kubernetesClient;

    @Override
    public List<K8sResourceSummary> listDeployments(String namespace) {
        return kubernetesClient.apps().deployments().inNamespace(resolveNamespace(namespace))
                .list().getItems().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    public List<K8sResourceSummary> listPods(String namespace) {
        return kubernetesClient.pods().inNamespace(resolveNamespace(namespace))
                .list().getItems().stream()
                .map(this::toSummary)
                .collect(Collectors.toList());
    }

    @Override
    public String getResourceYaml(String namespace, String kind, String name) {
        HasMetadata resource = fetchResource(resolveNamespace(namespace), kind, name);
        if (resource == null) {
            throw new IllegalArgumentException("未找到资源: " + kind + "/" + name);
        }
        return Serialization.asYaml(resource);
    }

    @Override
    public void createResource(String namespace, String yamlContent) {
        HasMetadata resource = Serialization.unmarshal(yamlContent, HasMetadata.class);
        applyNamespace(resource, resolveNamespace(namespace));
        kubernetesClient.resource(resource)
                .inNamespace(resource.getMetadata().getNamespace())
                .create();
    }

    @Override
    public void updateResource(String namespace, String kind, String name, String yamlContent) {
        HasMetadata resource = Serialization.unmarshal(yamlContent, HasMetadata.class);
        applyNamespace(resource, resolveNamespace(namespace));
        kubernetesClient.resource(resource)
                .inNamespace(resource.getMetadata().getNamespace())
                .createOrReplace();
    }

    @Override
    public void deleteResource(String namespace, String kind, String name) {
        HasMetadata resource = fetchResource(resolveNamespace(namespace), kind, name);
        if (resource != null) {
            kubernetesClient.resource(resource).delete();
        } else {
            throw new IllegalArgumentException("未找到资源: " + kind + "/" + name);
        }
    }

    private String resolveNamespace(String namespace) {
        if (StringUtils.hasText(namespace)) {
            return namespace;
        }
        String configured = kubernetesClient.getConfiguration().getNamespace();
        return StringUtils.hasText(configured) ? configured : "default";
    }

    private HasMetadata fetchResource(String namespace, String kind, String name) {
        String normalizedKind = kind == null ? "" : kind.trim().toLowerCase();
        return switch (normalizedKind) {
            case "deployment" -> kubernetesClient.apps().deployments().inNamespace(namespace).withName(name).get();
            case "statefulset" -> kubernetesClient.apps().statefulSets().inNamespace(namespace).withName(name).get();
            case "configmap" -> kubernetesClient.configMaps().inNamespace(namespace).withName(name).get();
            case "secret" -> kubernetesClient.secrets().inNamespace(namespace).withName(name).get();
            case "pod" -> kubernetesClient.pods().inNamespace(namespace).withName(name).get();
            default -> throw new IllegalArgumentException("暂不支持的资源类型: " + kind);
        };
    }

    private void applyNamespace(HasMetadata resource, String namespace) {
        if (resource.getMetadata() == null) {
            throw new IllegalArgumentException("资源缺少 metadata 信息");
        }
        if (!StringUtils.hasText(resource.getMetadata().getNamespace())) {
            resource.getMetadata().setNamespace(namespace);
        }
    }

    private K8sResourceSummary toSummary(Deployment deployment) {
        Integer replicas = Optional.ofNullable(deployment.getSpec())
                .map(spec -> spec.getReplicas())
                .orElse(0);
        String status = Optional.ofNullable(deployment.getStatus())
                .map(st -> String.format("ready %d/%d", st.getReadyReplicas() == null ? 0 : st.getReadyReplicas(), st.getReplicas() == null ? 0 : st.getReplicas()))
                .orElse("unknown");
        return new K8sResourceSummary(
                deployment.getMetadata().getName(),
                deployment.getMetadata().getNamespace(),
                "Deployment",
                replicas,
                status
        );
    }

    private K8sResourceSummary toSummary(Pod pod) {
        String status = Optional.ofNullable(pod.getStatus())
                .map(st -> st.getPhase())
                .orElse("unknown");
        return new K8sResourceSummary(
                pod.getMetadata().getName(),
                pod.getMetadata().getNamespace(),
                "Pod",
                null,
                status
        );
    }
}
