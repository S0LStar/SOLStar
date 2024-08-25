import './UnderBar.css';
import { useLocation, useNavigate } from 'react-router-dom';

// import icons
import MainIcon from '../../assets/common/Main.png';
import ActiveMainIcon from '../../assets/common/ActiveMain.png';
import ZzimIcon from '../../assets/common/Zzim.png';
import ActiveZzimIcon from '../../assets/common/ActiveZzim.png';
import WalletIcon from '../../assets/common/Wallet.png';
import ActiveWalletIcon from '../../assets/common/ActiveWallet.png';
import MyIcon from '../../assets/common/My.png';
import ActiveMyIcon from '../../assets/common/ActiveMy.png';

function UnderBar() {
  const navigate = useNavigate();
  const location = useLocation();

  const isActive = (path) => location.pathname === path;

  return (
    <>
      <div className="underbar">
        <button
          onClick={() => navigate('/')}
          className={`underbar-button ${isActive('/') ? 'active' : ''}`}
        >
          <img
            src={isActive('/') ? ActiveMainIcon : MainIcon}
            alt="main"
            className="underbar-icon"
          />
          <span>메인</span>
        </button>
        <button
          onClick={() => navigate('/zzim')}
          className={`underbar-button ${isActive('/zzim') ? 'active' : ''}`}
        >
          <img
            src={isActive('/zzim') ? ActiveZzimIcon : ZzimIcon}
            alt="zzim"
            className="underbar-icon"
          />
          <span>찜</span>
        </button>
        <button
          onClick={() => navigate('/wallet')}
          className={`underbar-button ${isActive('/wallet') ? 'active' : ''}`}
        >
          <img
            src={isActive('/wallet') ? ActiveWalletIcon : WalletIcon}
            alt="wallet"
            className="underbar-icon"
          />
          <span>지갑</span>
        </button>
        <button
          onClick={() => navigate('/my')}
          className={`underbar-button ${isActive('/my') ? 'active' : ''}`}
        >
          <img
            src={isActive('/my') ? ActiveMyIcon : MyIcon}
            alt="my"
            className="underbar-icon"
          />
          <span>마이</span>
        </button>
      </div>
    </>
  );
}

export default UnderBar;
