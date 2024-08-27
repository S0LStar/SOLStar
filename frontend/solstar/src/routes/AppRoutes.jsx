import { Route, Routes } from 'react-router-dom';

// Main
import MainPage from '../pages/MainPage';
import MainContainer from '../components/main/MainContainer';
import PopularFundingContainer from '../components/main/popular/PopularFundingContainer';
import MyArtistFundingContainer from '../components/main/myartist/MyArtistFundingContainer';

// Funding
import FundingPage from '../pages/FundingPage';
import FundingRegist from '../components/funding/regist/FundingRegist';
import FundingSearchResult from '../components/funding/search/FundingSearchResult';

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
import SignUpPage from '../pages/SignUpPage';
import SignUpContainer from '../components/signup/SignUpContainer';
import TermOfService from '../components/signup/accountRegist/TermOfService';
import CreateAccount from '../components/signup/accountRegist/CreateAccount';
import AccountRegistration from '../components/signup/accountRegist/AccountRegistration';
import AccountVerification from '../components/signup/accountRegist/AccountVerification';
import SetPassword from '../components/signup/accountRegist/SetPassword';
import ResetPassword from '../components/signup/accountRegist/ResetPassword';
import CreatedAccount from '../components/signup/accountRegist/CreatedAccount';


function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />}>
        <Route index element={<MainContainer />} />
        <Route path="search" element={<MainSearch />} />
        <Route path="popular" element={<PopularFundingContainer />} />
        <Route path="my-artist" element={<MyArtistFundingContainer />}></Route>
      </Route>

      <Route path="/funding">
        <Route path=":id" element={<FundingPage />} />
        <Route path="regist" element={<FundingRegist />} />
        <Route path="search" element={<FundingSearchResult />}></Route>
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
      <Route path="/signup" element={<SignUpPage />}>
        <Route index element={<TermOfService />}></Route>
        {/* <Route index element={<SignUpContainer />}></Route> */}
        <Route path="term" element={<TermOfService />} />
        <Route path="create" element={<CreateAccount />} />
        <Route path="regist" element={<AccountRegistration />} />
        <Route path="verify" element={<AccountVerification />} />
        <Route path="set" element={<SetPassword />} />
        <Route path="reset" element={<ResetPassword />} />
        <Route path="created" element={<CreatedAccount />} />
      </Route>
    </Routes>
  );
}

export default AppRoutes;
