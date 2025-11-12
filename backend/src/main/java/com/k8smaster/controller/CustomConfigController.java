package com.k8smaster.controller;

import com.k8smaster.common.Result;
import com.k8smaster.domain.dto.CreateCustomConfigRequest;
import com.k8smaster.domain.dto.CustomConfigResponse;
import com.k8smaster.domain.dto.UpdateCustomConfigRequest;
import com.k8smaster.service.CustomConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/configs")
@RequiredArgsConstructor
public class CustomConfigController {

    private final CustomConfigService customConfigService;

    @PostMapping
    public Result<Long> create(@Valid @RequestBody CreateCustomConfigRequest request) {
        return Result.success(customConfigService.createConfig(request));
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateCustomConfigRequest request) {
        customConfigService.updateConfig(id, request);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customConfigService.deleteConfig(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<CustomConfigResponse> get(@PathVariable Long id) {
        return Result.success(customConfigService.getConfig(id));
    }

    @GetMapping
    public Result<List<CustomConfigResponse>> list(@RequestParam(required = false) String keyword) {
        return Result.success(customConfigService.listConfigs(keyword));
    }
}
