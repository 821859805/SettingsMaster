package com.k8smaster.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateResourceRequest(
        @NotBlank String namespace,
        @NotBlank String yamlContent
) {
}
