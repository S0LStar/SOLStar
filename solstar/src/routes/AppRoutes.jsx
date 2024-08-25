import { Route, Routes } from 'react-router-dom';

// Main
import MainPage from '../pages/MainPage';
import MainContainer from '../components/main/MainContainer';
import WalletPage from '../pages/WalletPage';
import WalletContainer from '../components/wallet/WalletContainer';
import ZzimPage from '../pages/ZzimPage';
import ZzimContainer from '../components/zzim/ZzimContainer';
import MyPage from '../pages/MyPage';
import MyPageContainer from '../components/mypage/MyContainer';
import LoginPage from '../pages/LoginPage';
import LoginContainer from '../components/login/LoginContainer';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />}>
        <Route index element={<MainContainer />}></Route>
      </Route>
      <Route path="/wallet" element={<WalletPage />}>
        <Route index element={<WalletContainer />}></Route>
      </Route>
      <Route path="/zzim" element={<ZzimPage />}>
        <Route index element={<ZzimContainer />}></Route>
      </Route>
      <Route path="/my" element={<MyPage />}>
        <Route index element={<MyPageContainer />}></Route>
      </Route>
      <Route path="/login" element={<LoginPage />}>
        <Route index element={<LoginContainer />}></Route>
      </Route>
    </Routes>
  );
}

export default AppRoutes;
