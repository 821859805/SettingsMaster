import { http } from './http';
import type {
  CreateCustomConfigPayload,
  CustomConfig,
  UpdateCustomConfigPayload
} from '../types/customConfig';

export const fetchCustomConfigs = async (keyword?: string) => {
  const { data } = await http.get<CustomConfig[]>('/configs', { params: { keyword } });
  return data;
};

export const fetchCustomConfig = async (id: number) => {
  const { data } = await http.get<CustomConfig>(`/configs/${id}`);
  return data;
};

export const createCustomConfig = async (payload: CreateCustomConfigPayload) => {
  const { data } = await http.post<number>('/configs', payload);
  return data;
};

export const updateCustomConfig = async (id: number, payload: UpdateCustomConfigPayload) => {
  await http.put(`/configs/${id}`, payload);
};

export const deleteCustomConfig = async (id: number) => {
  await http.delete(`/configs/${id}`);
};
