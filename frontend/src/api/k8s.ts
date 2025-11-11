import { http } from './http';
import type { DeployResourcePayload, K8sResourceSummary, UpdateResourcePayload } from '../types/k8s';

export const fetchDeployments = async (namespace: string) => {
  const { data } = await http.get<K8sResourceSummary[]>(`/k8s/namespaces/${namespace}/deployments`);
  return data;
};

export const fetchPods = async (namespace: string) => {
  const { data } = await http.get<K8sResourceSummary[]>(`/k8s/namespaces/${namespace}/pods`);
  return data;
};

export const fetchResourceYaml = async (namespace: string, kind: string, name: string) => {
  const { data } = await http.get<string>(`/k8s/namespaces/${namespace}/resources/${kind}/${name}`);
  return data;
};

export const createResource = async (payload: DeployResourcePayload) => {
  await http.post('/k8s/resources', payload);
};

export const updateResource = async (kind: string, name: string, payload: UpdateResourcePayload) => {
  await http.put(`/k8s/resources/${kind}/${name}`, payload);
};

export const deleteResource = async (kind: string, name: string, namespace?: string) => {
  await http.delete(`/k8s/resources/${kind}/${name}`, { params: { namespace } });
};
