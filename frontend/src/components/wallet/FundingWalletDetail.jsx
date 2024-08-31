import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import AxiosInstance from '../../util/AxiosInstance'; // AxiosInstance import
import './FundingWalletDetail.css';
import Wallet from '../../assets/wallet/Wallet.png';
import FundingWallet from '../../assets/wallet/FundingWallet.png';
import Shinhan from '../../assets/wallet/Shinhan.png';
import BackButton from '../common/BackButton';

function FundingWalletDetail() {
  const navigate = useNavigate();
  const location = useLocation();
  const [transactionHistory, setTransactionHistory] = useState([]); // 거래 내역 상태 초기화
  const [loading, setLoading] = useState(true); // 로딩 상태 추가
  const [error, setError] = useState(null); // 에러 상태 추가

  // Access the walletData from the location state
  const walletData = location.state?.walletData;

  useEffect(() => {
    if (walletData) {
      const fetchTransactionHistory = async () => {
        try {
          const response = await AxiosInstance.get(
            `/wallet/funding/${walletData.id}`
          );
          setTransactionHistory(response.data.data || []); // 거래 내역 상태 업데이트, 데이터가 없으면 빈 배열로 설정
          console.log(response);
        } catch (error) {
          console.error('거래 내역을 가져오는 데 실패했습니다:', error);
          setError('거래 내역을 가져오는 데 실패했습니다.');
        } finally {
          setLoading(false); // 로딩 상태 업데이트
        }
      };

      fetchTransactionHistory();
    } else {
      setLoading(false); // walletData가 없을 때 로딩 상태 업데이트
      setError('지갑 정보를 찾을 수 없습니다.');
    }
  }, [walletData]);

  if (loading) {
    return <div>Loading...</div>; // 로딩 중일 때 표시
  }

  if (error) {
    return <div>{error}</div>; // 에러가 발생했을 때 표시
  }

  if (!walletData) {
    return <div>Wallet not found</div>; // walletData가 없는 경우
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
        <div className="fundingwalletdetail-header">
          <BackButton />
          <div className="fundingwalletdetail-header-description">
            펀딩 지갑
          </div>
        </div>
        <div className="fundingwalletdetail-item">
          <div className="fundingwalletdetail-background">
            <div className="fundingwalletdetail-owner">
              {walletData.artistName} 펀딩 계좌 잔액
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
              <button
                className="fundingwalletdetail-withdrawbutton"
                onClick={() => {
                  navigate(`/wallet/${walletData.id}/transfer`, {
                    state: { walletData },
                  });
                }}
              >
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
        <div className="transaction-history">
          {transactionHistory.length > 0 ? (
            transactionHistory.map((transaction, index) => (
              <div key={index} className="transaction-item">
                <div className="transaction-time">
                  {transaction.transactionDateTime}
                </div>
                <div className="transaction-summary">
                  {transaction.transactionSummary}
                </div>
                <div className="transaction-balance">
                  {transaction.transactionTypeName}&nbsp;
                  <span
                    className={`transaction-balance-main ${
                      transaction.transactionTypeName === '입금'
                        ? 'blue-text'
                        : 'red-text'
                    }`}
                  >
                    {transaction.transactionBalance.toLocaleString()}
                  </span>
                  &nbsp;원
                </div>
                <div className="transaction-afterbalance">
                  잔액&nbsp;
                  <span className="transaction-afterbalance-main">
                    {transaction.transactionAfterBalance.toLocaleString()}
                  </span>
                  &nbsp; 원
                </div>
              </div>
            ))
          ) : (
            <div>거래 내역이 없습니다.</div>
          )}
        </div>
      </div>
    </>
  );
}

export default FundingWalletDetail;
