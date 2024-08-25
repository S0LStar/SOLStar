import { Route, Routes } from 'react-router-dom';

// Main
import MainPage from '../pages/MainPage';
import MainContainer from '../components/main/MainContainer';

// Funding
import FundingPage from '../pages/FundingPage';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainPage />}>
        <Route index element={<MainContainer />}></Route>
      </Route>

      <Route path='/funding/:id' element={<FundingPage />}>
      </Route>
    </Routes>
  );
}

export default AppRoutes;
