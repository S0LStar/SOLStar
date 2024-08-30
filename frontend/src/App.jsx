import './App.css';
import { useLocation } from 'react-router-dom';
import Routes from './routes/AppRoutes';
import UnderBar from '../src/components/common/UnderBar';

function App() {
  const location = useLocation();
  const excludeFooterPaths = [
    '/login',
    '/signup',
    '/signup/term',
    '/signup/create',
    '/signup/regist',
    '/signup/verify',
    '/signup/set',
    '/signup/reset',
    '/signup/created',
  ];

  return (
    <>
      <Routes />
      {/* <UnderBar /> */}
      {!excludeFooterPaths.includes(location.pathname) && <UnderBar />}
    </>
  );
}

export default App;
