package com.example.k8sconfig.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCustomConfigRequest(
        @NotBlank String name,
        @NotBlank String namespace,
        @NotBlank String resourceType,
        @NotBlank String yamlContent,
        String description
) {
}
