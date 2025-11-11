package com.k8smaster.controller;

import com.k8smaster.common.Result;
import com.k8smaster.domain.dto.DeployResourceRequest;
import com.k8smaster.domain.dto.K8sResourceSummary;
import com.k8smaster.domain.dto.UpdateResourceRequest;
import com.k8smaster.service.KubernetesService;
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
@RequestMapping("/api/k8s")
@RequiredArgsConstructor
public class KubernetesController {

    private final KubernetesService kubernetesService;

    @GetMapping("/namespaces/{namespace}/deployments")
    public Result<List<K8sResourceSummary>> listDeployments(@PathVariable String namespace) {
        return Result.success(kubernetesService.listDeployments(namespace));
    }

    @GetMapping("/namespaces/{namespace}/pods")
    public Result<List<K8sResourceSummary>> listPods(@PathVariable String namespace) {
        return Result.success(kubernetesService.listPods(namespace));
    }

    @GetMapping("/namespaces/{namespace}/resources/{kind}/{name}")
    public Result<String> getResourceYaml(@PathVariable String namespace,
                                          @PathVariable String kind,
                                          @PathVariable String name) {
        return Result.success(kubernetesService.getResourceYaml(namespace, kind, name));
    }

    @PostMapping("/resources")
    public Result<Void> createResource(@Valid @RequestBody DeployResourceRequest request) {
        kubernetesService.createResource(request.namespace(), request.yamlContent());
        return Result.success();
    }

    @PutMapping("/resources/{kind}/{name}")
    public Result<Void> updateResource(@PathVariable String kind,
                                       @PathVariable String name,
                                       @Valid @RequestBody UpdateResourceRequest request) {
        kubernetesService.updateResource(request.namespace(), kind, name, request.yamlContent());
        return Result.success();
    }

    @DeleteMapping("/resources/{kind}/{name}")
    public Result<Void> deleteResource(@PathVariable String kind,
                                       @PathVariable String name,
                                       @RequestParam(required = false) String namespace) {
        kubernetesService.deleteResource(namespace, kind, name);
        return Result.success();
    }
}
