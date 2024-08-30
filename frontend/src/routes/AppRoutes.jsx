import { Route, Routes } from 'react-router-dom';
import PrivateRoute from './PrivateRoute'; // PrivateRoute import

// Main
import MainPage from '../pages/MainPage';
import MainContainer from '../components/main/MainContainer';
import PopularFundingContainer from '../components/main/popular/PopularFundingContainer';
import MyArtistFundingContainer from '../components/main/myartist/MyArtistFundingContainer';

// Funding
import FundingPage from '../pages/FundingPage';
import FundingDetail from '../components/funding/detail/FundingDetail';
import FundingRegist from '../components/funding/regist/FundingRegist';
import FundingSearchResult from '../components/funding/search/FundingSearchResult';

// Notice
import FundingNoticeRegist from '../components/funding/detail/FundingNoticeRegist';

// Artist
import ArtistFunding from '../components/artistfunding/ArtistFunding.jsx';

import WalletPage from '../pages/WalletPage';
import WalletContainer from '../components/wallet/WalletContainer';
import FundingWalletDetail from '../components/wallet/FundingWalletDetail';
import ZzimPage from '../pages/ZzimPage';
import ZzimContainer from '../components/zzim/ZzimContainer';
import MyPage from '../pages/MyPage';
import MyPageContainer from '../components/mypage/MyContainer';
import ViewProfile from '../components/mypage/ViewProfile.jsx';
import ParticipantFunding from '../components/mypage/ParticipantFunding';
import CreatedFunding from '../components/mypage/CreatedFunding';
import AgencyMyPageContainer from '../components/mypage/AgencyMyPageContainer';
import RequestFunding from '../components/mypage/RequestFunding';
import MyArtist from '../components/mypage/MyArtist';
import MyArtistFunding from '../components/mypage/MyArtistFunding';
import LoginPage from '../pages/LoginPage';
import LoginContainer from '../components/login/LoginContainer.jsx';
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
      {/* 로그인 및 회원가입 페이지는 보호하지 않음 */}
      <Route path="/login" element={<LoginPage />}>
        <Route index element={<LoginContainer />} />
      </Route>
      <Route path="/signup" element={<SignUpPage />}>
        <Route index element={<TermOfService />} />
        {/* <Route index element={<SignUpContainer />} /> */}
        <Route path="term" element={<TermOfService />} />
        <Route path="create" element={<CreateAccount />} />
        <Route path="regist" element={<AccountRegistration />} />
        <Route path="verify" element={<AccountVerification />} />
        <Route path="set" element={<SetPassword />} />
        <Route path="reset" element={<ResetPassword />} />
        <Route path="created" element={<CreatedAccount />} />
      </Route>

      {/* 나머지 모든 경로는 PrivateRoute로 보호 */}
      <Route
        path="*"
        element={
          <PrivateRoute>
            <Routes>
              <Route path="/" element={<MainPage />}>
                <Route index element={<MainContainer />} />
                <Route path="search" element={<MainSearch />} />
                <Route path="popular" element={<PopularFundingContainer />} />
                <Route
                  path="my-artist"
                  element={<MyArtistFundingContainer />}
                ></Route>
              </Route>

              <Route path="/funding" element={<FundingPage />}>
                <Route path=":fundingId" element={<FundingDetail />} />
                <Route
                  path=":fundingId/notice"
                  element={<FundingNoticeRegist />}
                />
                <Route path="regist" element={<FundingRegist />} />
                <Route path="search" element={<FundingSearchResult />}></Route>
              </Route>

              <Route path="/artist">
                <Route path=":artistId" element={<ArtistFunding />}></Route>
              </Route>

              <Route path="/wallet" element={<WalletPage />}>
                <Route index element={<WalletContainer />} />
                <Route path=":id" element={<FundingWalletDetail />} />
              </Route>
              <Route path="/zzim" element={<ZzimPage />}>
                <Route index element={<ZzimContainer />} />
              </Route>

              <Route path="/my" element={<MyPage />}>
                <Route index element={<MyPageContainer />} />
                <Route path="viewprofile" element={<ViewProfile />} />
                <Route
                  path="participantfunding"
                  element={<ParticipantFunding />}
                />
                <Route path="createdfunding" element={<CreatedFunding />} />
              </Route>

              <Route path="/agencymy" element={<MyPage />}>
                <Route index element={<AgencyMyPageContainer />} />
                <Route path="request" element={<RequestFunding />} />
                <Route path="myartist" element={<MyArtist />} />
                <Route path="myartistfunding" element={<MyArtistFunding />} />
              </Route>
            </Routes>
          </PrivateRoute>
        }
      />
    </Routes>
  );
}

export default AppRoutes;
