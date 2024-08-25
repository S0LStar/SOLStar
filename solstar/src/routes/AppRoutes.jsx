import { Route, Routes } from 'react-router-dom';

// Main
import MainPage from '../pages/MainPage';
import MainContainer from '../components/main/MainContainer';

import PopularFundingContainer from '../components/main/popular/PopularFundingContainer';
// Funding
import FundingPage from '../pages/FundingPage';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />}>
        <Route index element={<MainContainer />} />
        <Route path="popular" element={<PopularFundingContainer />}></Route>
      </Route>

      <Route path="/funding">
        <Route path=":id" element={<FundingPage />} />
      </Route>
    </Routes>
  );
}

export default AppRoutes;
