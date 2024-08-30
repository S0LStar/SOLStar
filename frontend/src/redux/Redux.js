import { configureStore } from '@reduxjs/toolkit';
import authReducer, { setToken } from './slices/authSlice';
// 필요한 다른 리듀서를 임포트합니다.
// import userReducer from './slices/userSlice';
// import accountReducer from './slices/accountSlice';
// import linkedAccountReducer from './slices/linkedAccount';

const store = configureStore({
  reducer: {
    auth: authReducer,
    // user: userReducer,
    // account: accountReducer,
    // linkedAccount: linkedAccountReducer,
  },
});

const accessToken = localStorage.getItem('accessToken');
const refreshToken = localStorage.getItem('refreshToken');
const role = localStorage.getItem('role');
if (accessToken && refreshToken && role) {
  store.dispatch(setToken({ accessToken, refreshToken, role }));
}

export default store;
