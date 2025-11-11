package com.k8smaster.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record DeployResourceRequest(
        @NotBlank String namespace,
        @NotBlank String yamlContent
) {
}
