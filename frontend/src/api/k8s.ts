import { http } from './http';
import type { Result } from '../types/common';
import type { DeployResourcePayload, K8sResourceSummary, UpdateResourcePayload } from '../types/k8s';

export const fetchDeployments = async (namespace: string) => {
  const { data } = await http.get<Result<K8sResourceSummary[]>>(`/k8s/namespaces/${namespace}/deployments`);
  return data.data ?? [];
};

export const fetchPods = async (namespace: string) => {
  const { data } = await http.get<Result<K8sResourceSummary[]>>(`/k8s/namespaces/${namespace}/pods`);
  return data.data ?? [];
};

export const fetchResourceYaml = async (namespace: string, kind: string, name: string) => {
  const { data } = await http.get<Result<string>>(`/k8s/namespaces/${namespace}/resources/${kind}/${name}`);
  return data.data ?? '';
};

export const createResource = async (payload: DeployResourcePayload) => {
  await http.post<Result<void>>('/k8s/resources', payload);
};

export const updateResource = async (kind: string, name: string, payload: UpdateResourcePayload) => {
  await http.put<Result<void>>(`/k8s/resources/${kind}/${name}`, payload);
};

export const deleteResource = async (kind: string, name: string, namespace?: string) => {
  await http.delete<Result<void>>(`/k8s/resources/${kind}/${name}`, { params: { namespace } });
};
