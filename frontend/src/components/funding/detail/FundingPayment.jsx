import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import PropTypes from 'prop-types';
import './FundingPayment.css';

import DefaultImage from '../../../assets/character/Sol.png';

function FundingPayment({ artistName, artistProfileImage }) {
  const [payment, setPayment] = useState({
    accountBalance: '',
    transactionHistory: [],
  });
  const { fundingId } = useParams();

  useEffect(() => {
    console.log(artistName);
    // TODO : 펀딩 계좌 상세 조회 API 연결 (/api/wallet/funding-detail)
    // request body : fundingId
    const fetchFundingPaymnet = {
      transactionHistory: [
        {
          transactionDate: '20240401',
          transactionTime: '102447',
          transactionBalance: 1000000,
          transactionSummary: '(주) 삼성전자',
          transactionAfterBalance: 2000000,
          transactionTypeName: '입금',
        },
        {
          transactionDate: '20240401',
          transactionTime: '102447',
          transactionBalance: 1000000,
          transactionAfterBalance: 1000000,
          transactionSummary: '(주) 삼성전자',
          transactionTypeName: '입금',
        },
      ],
    };

    // 날짜와 시간을 처리해 새로운 transactionDateTime으로 변환
    const filteredFundingPayment = {
      accountBalance:
        fetchFundingPaymnet.transactionHistory[0].transactionAfterBalance,
      transactionHistory: fetchFundingPaymnet.transactionHistory.map(
        (history) => {
          const formattedDate = `${history.transactionDate.slice(2, 4)}/${history.transactionDate.slice(4, 6)}/${history.transactionDate.slice(6, 8)}`;
          const formattedTime = `${history.transactionTime.slice(0, 2)}:${history.transactionTime.slice(2, 4)}`;

          return {
            ...history,
            transactionDateTime: `${formattedDate} ${formattedTime}`,
          };
        }
      ),
    };

    setPayment(filteredFundingPayment);
  }, [artistName]);

  return (
    <div className="funding-payment-container">
      <div className="funding-payment-balance">
        <img src={DefaultImage} alt="" className="funding-payment-image" />
        <div>
          <div>{artistName} 펀딩 계좌</div>
          <div>₩ {payment.accountBalance}</div>
        </div>
      </div>
      <div className="funding-payment-list">
        {payment.transactionHistory.map((history, idx) => (
          <div key={idx} className="funding-payment-item">
            <div className="funding-payment-main">
              <div>{history.transactionTypeName}</div>
              <div>{history.transactionDateTime}</div>
            </div>
            <div className="funding-payment-detail">
              <div>{history.transactionSummary}</div>
              <div>{history.transactionBalance.toLocaleString()} 원</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

FundingPayment.propTypes = {
  artistName: PropTypes.string.isRequired,
  artistProfileImage: PropTypes.node.isRequired,
};

export default FundingPayment;
