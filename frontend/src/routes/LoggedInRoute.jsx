import React from 'react';
import { Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

const LoggedInRoute = ({ children }) => {
  const accessToken = useSelector((state) => state.auth.accessToken);

  return accessToken ? <Navigate to="/" /> : children;
};

export default LoggedInRoute;
