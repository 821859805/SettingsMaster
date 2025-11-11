package com.example.k8sconfig.dto;

public record K8sResourceSummary(
        String name,
        String namespace,
        String type,
        Integer replicas,
        String status
) {
}
