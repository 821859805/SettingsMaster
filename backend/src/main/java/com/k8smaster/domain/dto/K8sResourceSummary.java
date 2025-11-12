package com.k8smaster.domain.dto;

public record K8sResourceSummary(
        String name,
        String namespace,
        String type,
        Integer replicas,
        String status
) {
}
