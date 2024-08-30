import React, { useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import { useSelector } from 'react-redux';

const PrivateRoute = ({ children }) => {
  const accessToken = useSelector((state) => state.auth.accessToken);
  const [isTokenLoaded, setIsTokenLoaded] = useState(false);

  useEffect(() => {
    // Redux 상태 로딩 완료 시 토큰 상태 업데이트
    setIsTokenLoaded(true);
  }, [accessToken]);

  if (!isTokenLoaded) {
    // 토큰이 로드될 때까지 대기
    return <div>Loading...</div>;
  }

  return accessToken ? children : <Navigate to="/login" />;
};

export default PrivateRoute;
