import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import AxiosInstance from '../../util/AxiosInstance'; // AxiosInstance import
import './AccountTransfer.css';
import Wallet from '../../assets/wallet/Wallet.png';
import FundingWallet from '../../assets/wallet/FundingWallet.png';
import Shinhan from '../../assets/wallet/Shinhan.png';
import WideButton from '../common/WideButton';
import BackButton from '../common/BackButton';

function AccountTransfer() {
  const location = useLocation();
  const [transactionHistory, setTransactionHistory] = useState([]); // 거래 내역 상태 초기화
  const [loading, setLoading] = useState(true); // 로딩 상태 추가
  const [error, setError] = useState(null); // 에러 상태 추가

  const [accountNumber, setAccountNumber] = useState(''); // 계좌번호 입력 상태
  const [balance, setBalance] = useState(''); // 금액 입력 상태
  const [memo, setMemo] = useState(''); // 메모 입력 상태

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

  const handleTransfer = async () => {
    // 이체 로직
    if (!balance || !memo) {
      alert('금액과 메모를 입력해주세요.');
      return;
    }

    try {
      const response = await AxiosInstance.post('/funding/use-money', {
        fundingId: walletData.id,
        balance,
        storeAccount: memo,
        storeSummary: memo,
      });
      console.log('이체 성공:', response.data);
      alert('이체가 완료되었습니다.');

      // 이체 후 상태 초기화 또는 필요한 후처리 로직 추가
      setAccountNumber('');
      setBalance('');
      setMemo('');
    } catch (error) {
      console.error('이체 실패:', error);
      alert('이체에 실패했습니다.');
    }
  };

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
      <div className="accounttransfer-container">
        <div className="accounttransfer-header">
          <BackButton />
          <div className="accounttransfer-header-description">펀딩 지갑</div>
        </div>
        <div className="accounttransfer-item">
          <div className="accounttransfer-background">
            <div className="accounttransfer-owner">
              {walletData.artistName} 펀딩 계좌 잔액
            </div>
            <img
              src={icon}
              alt={walletData.code}
              className="accounttransfer-icon"
            />
            <div className="accounttransfer-balance">
              {walletData.balance.toLocaleString()} 원
            </div>
            <img
              src={FundingWallet}
              alt="fundingwalletdetail Background"
              className="accounttransfer-bg-image"
            />
          </div>
        </div>
        <div className="accounttransfer-item">
          <div className="accounttransfer-background">
            <div className="accounttransfer-transfer-form">
              <input
                type="number"
                placeholder="금액"
                value={balance}
                onChange={(e) => setBalance(e.target.value)}
              />
              <input
                type="text"
                placeholder="메모"
                value={memo}
                onChange={(e) => setMemo(e.target.value)}
              />
              <button
                className="accounttransfer-withdrawbutton"
                onClick={handleTransfer}
              >
                이체
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default AccountTransfer;
