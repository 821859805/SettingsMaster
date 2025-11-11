export interface CustomConfig {
  id: number;
  name: string;
  namespace: string;
  resourceType: string;
  yamlContent: string;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCustomConfigPayload {
  name: string;
  namespace: string;
  resourceType: string;
  yamlContent: string;
  description?: string;
}

export interface UpdateCustomConfigPayload extends CreateCustomConfigPayload {}
