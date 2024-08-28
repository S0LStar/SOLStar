import './WalletContainer.css';
import { useNavigate } from 'react-router-dom';
import WalletItem from './WalletItem.jsx';
import FundingWalletItem from './FundingWalletItem.jsx';

function WalletContainer() {
  const navigate = useNavigate();

  const walletTempData = {
    name: '아무개',
    code: '신한',
    balance: 10000000,
  };

  const fundingWalletTempData = [
    {
      id: 1,
      name: '아무개1',
      code: '신한',
      balance: 10000000,
    },
    {
      id: 2,
      name: '아무개2',
      code: '국민',
      balance: 20000000,
    },
    {
      id: 3,
      name: '아무개3',
      code: '우리',
      balance: 30000000,
    },
  ];

  return (
    <>
      <div className="wallet-container">
        <div className="wallet-header">내 지갑</div>
        <WalletItem walletData={walletTempData} />

        <div className="wallet-header">펀딩 지갑</div>
        {fundingWalletTempData.map((walletData) => (
          <FundingWalletItem
            key={walletData.id}
            walletData={walletData}
            onClick={() =>
              navigate(`/wallet/${walletData.id}`, { state: { walletData } })
            }
          />
        ))}
      </div>
    </>
  );
}

export default WalletContainer;
