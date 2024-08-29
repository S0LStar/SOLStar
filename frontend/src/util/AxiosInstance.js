import axios from 'axios';
import store from '../redux/Redux'; // Redux store import 경로 수정
import { clearToken, setToken } from '../redux/slices/authSlice';

const API_LINK = 'http://localhost:8080/api';
// const API_LINK = loca

//
const axiosInstance = axios.create({
  baseURL: API_LINK,
  withCredentials: true, // 쿠키를 포함하여 요청
});

const EXCLUDED_PATHS = ['/auth'];

axiosInstance.interceptors.request.use(
  (config) => {
    const state = store.getState();
    const { accessToken } = state.auth;
    if (
      config.url && // 존재 여부를 먼저 확인
      !EXCLUDED_PATHS.some((path) => config.url.includes(path)) // Non-null assertion 연산자 제거
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

    if (error.response.status === 403 && !originalRequest._retry) {
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
        axiosInstance.defaults.headers.common['Authorization'] =
          `Bearer ${newAccessToken}`;
        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        store.dispatch(clearToken());
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
