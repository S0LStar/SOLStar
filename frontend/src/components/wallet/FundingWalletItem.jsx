import React from 'react';
import { useNavigate } from 'react-router-dom';
import './FundingWalletItem.css';
import Wallet from '../../assets/wallet/Wallet.png';
import FundingWallet from '../../assets/wallet/FundingWallet.png';
import Shinhan from '../../assets/wallet/Shinhan.png';

// 필요에 따라 더 많은 아이콘 추가 가능

function FundingWalletItem({ walletData, onClick }) {
  const navigate = useNavigate();
  let icon;

  // code에 따라 아이콘 선택
  switch (walletData.code) {
    case '신한':
      icon = Shinhan;
      break;
    // 다른 코드와 해당 아이콘에 대한 케이스 추가 가능
    default:
      icon = Wallet; // 기본 아이콘
  }

  return (
    <div className="fundingwallet-item">
      <div className="fundingwallet-background" onClick={onClick}>
        <div className="fundingwallet-owner">
          {walletData.artistName} 펀딩 계좌 잔액
        </div>
        <img src={icon} alt={walletData.code} className="fundingwallet-icon" />
        <div className="fundingwallet-balance">
          {walletData.balance.toLocaleString()} 원
        </div>
        <div className="fundingwallet-buttons">
          <button
            className="fundingwallet-withdrawbutton"
            onClick={() => {
              navigate(`/wallet/${walletData.fundingId}/transfer`);
            }}
          >
            이체
          </button>
        </div>
        <img
          src={FundingWallet}
          alt="fundingwallet Background"
          className="fundingwallet-bg-image"
        />
      </div>
    </div>
  );
}

export default FundingWalletItem;
