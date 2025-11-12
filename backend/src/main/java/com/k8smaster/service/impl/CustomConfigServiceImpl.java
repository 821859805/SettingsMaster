package com.k8smaster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.k8smaster.domain.dto.CreateCustomConfigRequest;
import com.k8smaster.domain.dto.CustomConfigResponse;
import com.k8smaster.domain.dto.UpdateCustomConfigRequest;
import com.k8smaster.domain.entity.CustomConfig;
import com.k8smaster.mapper.CustomConfigMapper;
import com.k8smaster.service.CustomConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        CustomConfig config = Optional.ofNullable(mapper.selectById(id))
                .orElseThrow(() -> new IllegalArgumentException("配置不存在: " + id));
        config.setName(request.name());
        config.setNamespace(request.namespace());
        config.setResourceType(request.resourceType());
        config.setYamlContent(request.yamlContent());
        config.setDescription(request.description());
        config.setUpdatedAt(LocalDateTime.now());
        mapper.updateById(config);
    }

    @Override
    public void deleteConfig(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public CustomConfigResponse getConfig(Long id) {
        CustomConfig config = Optional.ofNullable(mapper.selectById(id))
                .orElseThrow(() -> new IllegalArgumentException("配置不存在: " + id));
        return toResponse(config);
    }

    @Override
    public List<CustomConfigResponse> listConfigs(String keyword) {
        LambdaQueryWrapper<CustomConfig> wrapper = Wrappers.lambdaQuery(CustomConfig.class)
                .orderByDesc(CustomConfig::getUpdatedAt);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(q -> q.like(CustomConfig::getName, keyword)
                    .or()
                    .like(CustomConfig::getNamespace, keyword)
                    .or()
                    .like(CustomConfig::getResourceType, keyword));
        }
        return mapper.selectList(wrapper).stream()
                .map(this::toResponse)
                .toList();
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
