// src/util/AxiosInstance.js

import axios from 'axios';
import store from '../redux/redux'; // Redux store import
import { clearToken, setToken } from '../redux/slices/authSlice';

const API_LINK = 'http://localhost:8080/api';

const axiosInstance = axios.create({
  baseURL: API_LINK,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // 쿠키를 포함하여 요청
});

const EXCLUDED_PATHS = ['/auth'];

// 초기화 시 localStorage에서 토큰 가져오기
const accessToken = localStorage.getItem('accessToken');
const refreshToken = localStorage.getItem('refreshToken');

if (accessToken && refreshToken) {
  store.dispatch(setToken({ accessToken, refreshToken }));
  axiosInstance.defaults.headers.common['Authorization'] =
    `Bearer ${accessToken}`;
}

axiosInstance.interceptors.request.use(
  (config) => {
    const state = store.getState();
    const { accessToken } = state.auth;

    if (
      config.url &&
      !EXCLUDED_PATHS.some((path) => config.url.includes(path))
    ) {
      if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    const state = store.getState();

    if (
      error.response &&
      error.response.status === 403 &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true;

      try {
        const response = await axios.post(
          `${API_LINK}/auth/refresh`,
          {},
          { withCredentials: true }
        );
        const newAccessToken = response.data.data.accessToken;
        store.dispatch(
          setToken({
            accessToken: newAccessToken,
            refreshToken: state.auth.refreshToken,
          })
        );

        // localStorage에 새로운 토큰 저장
        localStorage.setItem('accessToken', newAccessToken);

        axiosInstance.defaults.headers.common['Authorization'] =
          `Bearer ${newAccessToken}`;
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        store.dispatch(clearToken());
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
