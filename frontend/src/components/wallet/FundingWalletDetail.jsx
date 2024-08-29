import React from 'react';
import { useParams, useLocation } from 'react-router-dom'; // Import useLocation
import './FundingWalletDetail.css';
import Wallet from '../../assets/wallet/Wallet.png';
import FundingWallet from '../../assets/wallet/FundingWallet.png';
import Shinhan from '../../assets/wallet/Shinhan.png';

function FundingWalletDetail() {
  const location = useLocation();

  // Access the walletData from the location state
  const walletData = location.state?.walletData;

  if (!walletData) {
    return <div>Wallet not found</div>;
  }

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
    <>
      <div className="fundingwalletdetail-container">
        <div className="fundingwalletdetail-item">
          <h2>펀딩 지갑</h2>
          <div className="fundingwalletdetail-background">
            <div className="fundingwalletdetail-owner">
              {walletData.name}님의 충전 계좌
            </div>
            <img
              src={icon}
              alt={walletData.code}
              className="fundingwalletdetail-icon"
            />
            <div className="fundingwalletdetail-balance">
              {walletData.balance.toLocaleString()} 원
            </div>
            <div className="fundingwalletdetail-buttons">
              <button className="fundingwalletdetail-calculatebutton">
                정산
              </button>
              <button className="fundingwalletdetail-withdrawbutton">
                이체
              </button>
            </div>
            <img
              src={FundingWallet}
              alt="fundingwalletdetail Background"
              className="fundingwalletdetail-bg-image"
            />
          </div>
        </div>

        <div>거래 내역</div>
        <hr />
      </div>
    </>
  );
}

export default FundingWalletDetail;
