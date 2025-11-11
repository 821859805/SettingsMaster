package com.example.k8sconfig.service.impl;

import com.example.k8sconfig.dto.CreateCustomConfigRequest;
import com.example.k8sconfig.dto.CustomConfigResponse;
import com.example.k8sconfig.dto.UpdateCustomConfigRequest;
import com.example.k8sconfig.entity.CustomConfig;
import com.example.k8sconfig.mapper.CustomConfigMapper;
import com.example.k8sconfig.service.CustomConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomConfigServiceImpl implements CustomConfigService {

    private final CustomConfigMapper mapper;

    @Override
    public Long createConfig(CreateCustomConfigRequest request) {
        CustomConfig config = new CustomConfig();
        config.setName(request.name());
        config.setNamespace(request.namespace());
        config.setResourceType(request.resourceType());
        config.setYamlContent(request.yamlContent());
        config.setDescription(request.description());
        LocalDateTime now = LocalDateTime.now();
        config.setCreatedAt(now);
        config.setUpdatedAt(now);
        mapper.insert(config);
        return config.getId();
    }

    @Override
    public void updateConfig(Long id, UpdateCustomConfigRequest request) {
        CustomConfig config = mapper.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("配置不存在: " + id));
        config.setName(request.name());
        config.setNamespace(request.namespace());
        config.setResourceType(request.resourceType());
        config.setYamlContent(request.yamlContent());
        config.setDescription(request.description());
        config.setUpdatedAt(LocalDateTime.now());
        mapper.update(config);
    }

    @Override
    public void deleteConfig(Long id) {
        mapper.delete(id);
    }

    @Override
    public CustomConfigResponse getConfig(Long id) {
        return mapper.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("配置不存在: " + id));
    }

    @Override
    public List<CustomConfigResponse> listConfigs(String keyword) {
        String normalized = StringUtils.hasText(keyword) ? keyword : null;
        return mapper.search(normalized)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CustomConfigResponse toResponse(CustomConfig config) {
        return new CustomConfigResponse(
                config.getId(),
                config.getName(),
                config.getNamespace(),
                config.getResourceType(),
                config.getYamlContent(),
                config.getDescription(),
                config.getCreatedAt(),
                config.getUpdatedAt()
        );
    }
}
