package com.example.k8sconfig.controller;

import com.example.k8sconfig.dto.DeployResourceRequest;
import com.example.k8sconfig.dto.K8sResourceSummary;
import com.example.k8sconfig.dto.UpdateResourceRequest;
import com.example.k8sconfig.service.KubernetesService;
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
@RequestMapping("/api/k8s")
@RequiredArgsConstructor
public class KubernetesController {

    private final KubernetesService kubernetesService;

    @GetMapping("/namespaces/{namespace}/deployments")
    public List<K8sResourceSummary> listDeployments(@PathVariable String namespace) {
        return kubernetesService.listDeployments(namespace);
    }

    @GetMapping("/namespaces/{namespace}/pods")
    public List<K8sResourceSummary> listPods(@PathVariable String namespace) {
        return kubernetesService.listPods(namespace);
    }

    @GetMapping("/namespaces/{namespace}/resources/{kind}/{name}")
    public String getResourceYaml(@PathVariable String namespace,
                                  @PathVariable String kind,
                                  @PathVariable String name) {
        return kubernetesService.getResourceYaml(namespace, kind, name);
    }

    @PostMapping("/resources")
    @ResponseStatus(HttpStatus.CREATED)
    public void createResource(@Valid @RequestBody DeployResourceRequest request) {
        kubernetesService.createResource(request.namespace(), request.yamlContent());
    }

    @PutMapping("/resources/{kind}/{name}")
    public void updateResource(@PathVariable String kind,
                               @PathVariable String name,
                               @Valid @RequestBody UpdateResourceRequest request) {
        kubernetesService.updateResource(request.namespace(), kind, name, request.yamlContent());
    }

    @DeleteMapping("/resources/{kind}/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteResource(@PathVariable String kind,
                               @PathVariable String name,
                               @RequestParam(required = false) String namespace) {
        kubernetesService.deleteResource(namespace, kind, name);
    }
}
