export interface K8sResourceSummary {
  name: string;
  namespace: string;
  type: string;
  replicas: number | null;
  status: string;
}

export interface DeployResourcePayload {
  namespace: string;
  yamlContent: string;
}

export interface UpdateResourcePayload {
  namespace: string;
  yamlContent: string;
}
