package com.example.k8sconfig.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomConfig {
    private Long id;
    private String name;
    private String namespace;
    private String resourceType;
    private String yamlContent;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
