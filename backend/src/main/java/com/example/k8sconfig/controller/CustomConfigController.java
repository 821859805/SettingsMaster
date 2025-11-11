package com.example.k8sconfig.controller;

import com.example.k8sconfig.dto.CreateCustomConfigRequest;
import com.example.k8sconfig.dto.CustomConfigResponse;
import com.example.k8sconfig.dto.UpdateCustomConfigRequest;
import com.example.k8sconfig.service.CustomConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/configs")
@RequiredArgsConstructor
public class CustomConfigController {

    private final CustomConfigService customConfigService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@Valid @RequestBody CreateCustomConfigRequest request) {
        return customConfigService.createConfig(request);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody UpdateCustomConfigRequest request) {
        customConfigService.updateConfig(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customConfigService.deleteConfig(id);
    }

    @GetMapping("/{id}")
    public CustomConfigResponse get(@PathVariable Long id) {
        return customConfigService.getConfig(id);
    }

    @GetMapping
    public List<CustomConfigResponse> list(@RequestParam(required = false) String keyword) {
        return customConfigService.listConfigs(keyword);
    }
}
