import { http } from './http';
import type { Result } from '../types/common';
import type {
  CreateCustomConfigPayload,
  CustomConfig,
  UpdateCustomConfigPayload
} from '../types/customConfig';

export const fetchCustomConfigs = async (keyword?: string) => {
  const { data } = await http.get<Result<CustomConfig[]>>('/configs', { params: { keyword } });
  return data.data ?? [];
};

export const fetchCustomConfig = async (id: number) => {
  const { data } = await http.get<Result<CustomConfig>>(`/configs/${id}`);
  return data.data!;
};

export const createCustomConfig = async (payload: CreateCustomConfigPayload) => {
  const { data } = await http.post<Result<number>>('/configs', payload);
  return data.data!;
};

export const updateCustomConfig = async (id: number, payload: UpdateCustomConfigPayload) => {
  await http.put<Result<void>>(`/configs/${id}`, payload);
};

export const deleteCustomConfig = async (id: number) => {
  await http.delete<Result<void>>(`/configs/${id}`);
};
