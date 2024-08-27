import React from 'react';
import './FunddingWalletItem.css';
import Wallet from '../../assets/wallet/Wallet.png';
import FunddingWallet from '../../assets/wallet/FunddingWallet.png';
import Shinhan from '../../assets/wallet/Shinhan.png';

// 필요에 따라 더 많은 아이콘 추가 가능

function FunddingWalletItem({ walletData }) {
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
    <div className="funddingwallet-item">
      <div className="funddingwallet-background">
        <div className="funddingwallet-owner">
          {walletData.name}님의 충전 계좌
        </div>
        <img src={icon} alt={walletData.code} className="funddingwallet-icon" />
        <div className="funddingwallet-balance">
          {walletData.balance.toLocaleString()} 원
        </div>
        <div className="funddingwallet-buttons">
          <button className="funddingwallet-calculatebutton">정산</button>
          <button className="funddingwallet-withdrawbutton">이체</button>
        </div>
        <img
          src={FunddingWallet}
          alt="funddingwallet Background"
          className="funddingwallet-bg-image"
        />
      </div>
    </div>
  );
}

export default FunddingWalletItem;
