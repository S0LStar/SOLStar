import './WalletContainer.css';
import WalletItem from './WalletItem.jsx';
import FunddingWalletItem from './FunddingWalletItem.jsx';

function WalletContainer() {
  const walletTempData = {
    name: '아무개',
    code: '신한',
    balance: 10000000,
  };

  const funddingWalletTempData = {
    name: '아무개',
    code: '신한',
    balance: 10000000,
  };

  return (
    <>
      <div className="wallet-container">
        <div className="wallet-header">내 지갑</div>
        <WalletItem walletData={walletTempData} />

        <div className="wallet-header">펀딩 지갑</div>
        <FunddingWalletItem walletData={funddingWalletTempData} />
      </div>
    </>
  );
}

export default WalletContainer;
