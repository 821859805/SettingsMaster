package com.example.k8sconfig.service;

import com.example.k8sconfig.dto.CreateCustomConfigRequest;
import com.example.k8sconfig.dto.CustomConfigResponse;
import com.example.k8sconfig.dto.UpdateCustomConfigRequest;

import java.util.List;

public interface CustomConfigService {
    Long createConfig(CreateCustomConfigRequest request);

    void updateConfig(Long id, UpdateCustomConfigRequest request);

    void deleteConfig(Long id);

    CustomConfigResponse getConfig(Long id);

    List<CustomConfigResponse> listConfigs(String keyword);
}
