package com.example.k8sconfig.dto;

import java.time.LocalDateTime;

public record CustomConfigResponse(
        Long id,
        String name,
        String namespace,
        String resourceType,
        String yamlContent,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
