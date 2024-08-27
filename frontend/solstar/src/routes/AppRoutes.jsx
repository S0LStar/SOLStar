import { Route, Routes } from 'react-router-dom';

// Main
import MainPage from '../pages/MainPage';
import MainContainer from '../components/main/MainContainer';

// Funding
import PopularFundingContainer from '../components/main/popular/PopularFundingContainer';
import FundingPage from '../pages/FundingPage';
import FundingRegist from '../components/funding/fundingRegist/FundingRegist';

import WalletPage from '../pages/WalletPage';
import WalletContainer from '../components/wallet/WalletContainer';
import ZzimPage from '../pages/ZzimPage';
import ZzimContainer from '../components/zzim/ZzimContainer';
import MyPage from '../pages/MyPage';
import MyPageContainer from '../components/mypage/MyContainer';
import EditProfileContainer from '../components/mypage/EditProfileContainer';
import LoginPage from '../pages/LoginPage';
import LoginContainer from '../components/login/LoginContainer';
import MainSearch from '../components/main/MainSearch';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />}>
        <Route index element={<MainContainer />} />
        <Route path="search" element={<MainSearch />} />
        <Route path="popular" element={<PopularFundingContainer />} />
      </Route>

      <Route path="/funding">
        <Route path=":id" element={<FundingPage />} />
        <Route path="regist" element={<FundingRegist />} />
      </Route>

      <Route path="/wallet" element={<WalletPage />}>
        <Route index element={<WalletContainer />}></Route>
      </Route>
      <Route path="/zzim" element={<ZzimPage />}>
        <Route index element={<ZzimContainer />}></Route>
      </Route>
      <Route path="/my" element={<MyPage />}>
        <Route index element={<MyPageContainer />}></Route>
        <Route path="editprofile" element={<EditProfileContainer />} />
      </Route>
      <Route path="/login" element={<LoginPage />}>
        <Route index element={<LoginContainer />}></Route>
      </Route>
    </Routes>
  );
}

export default AppRoutes;
