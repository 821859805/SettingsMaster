import axios from 'axios';

export const http = axios.create({
  baseURL: '/api',
  timeout: 10000
});

http.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.message ?? error.message;
    return Promise.reject(new Error(message));
  }
);
