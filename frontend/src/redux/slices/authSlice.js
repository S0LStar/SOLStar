// src/redux/slices/authSlice.js

import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  accessToken: null,
  refreshToken: null,
  role: null, // Add role field
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setToken: (state, action) => {
      state.accessToken = action.payload.accessToken;
      state.refreshToken = action.payload.refreshToken;
      state.role = action.payload.role;
    },
    clearToken: (state) => {
      state.accessToken = null;
      state.refreshToken = null;
      state.role = null;
    },
  },
});

export const { setToken, clearToken } = authSlice.actions;
export default authSlice.reducer;
