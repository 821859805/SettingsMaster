import axios from 'axios';
import type { Result } from '../types/common';

export const http = axios.create({
  baseURL: '/api',
  timeout: 10000
});

http.interceptors.response.use(
  (response) => {
    const result = response.data as Result<unknown> | undefined;
    if (result && typeof result.code === 'number' && result.code !== 0) {
      return Promise.reject(new Error(result.message ?? '请求失败'));
    }
    return response;
  },
  (error) => {
    const message = error.response?.data?.message ?? error.message;
    return Promise.reject(new Error(message));
  }
);
