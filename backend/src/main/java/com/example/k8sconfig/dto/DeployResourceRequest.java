package com.example.k8sconfig.dto;

import jakarta.validation.constraints.NotBlank;

public record DeployResourceRequest(
        @NotBlank String namespace,
        @NotBlank String yamlContent
) {
}
