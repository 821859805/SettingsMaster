package com.k8smaster.service;

import com.k8smaster.domain.dto.CreateCustomConfigRequest;
import com.k8smaster.domain.dto.CustomConfigResponse;
import com.k8smaster.domain.dto.UpdateCustomConfigRequest;

import java.util.List;

public interface CustomConfigService {
    Long createConfig(CreateCustomConfigRequest request);

    void updateConfig(Long id, UpdateCustomConfigRequest request);

    void deleteConfig(Long id);

    CustomConfigResponse getConfig(Long id);

    List<CustomConfigResponse> listConfigs(String keyword);
}
