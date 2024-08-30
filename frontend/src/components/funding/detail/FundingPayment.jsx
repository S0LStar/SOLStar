import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import PropTypes from 'prop-types';
import './FundingPayment.css';

import DefaultImage from '../../../assets/character/Sol.png';
import axiosInstance from '../../../util/AxiosInstance';
import Error from '../../common/Error';

function FundingPayment({ artistName, artistProfileImage }) {
  const [error, setError] = useState(false);
  const [payment, setPayment] = useState({
    accountBalance: '',
    transactionHistory: [],
  });

  const [filteredPayment, setFilteredPayment] = useState({
    accountBalance: '',
    transactionHistory: [],
  });
  const { fundingId } = useParams();

  // 데이터 페칭
  useEffect(() => {
    const fetchPayment = async () => {
      try {
        const response = await axiosInstance.post(
          `/wallet/funding/${fundingId}`
        );
        setPayment(response.data.data);
        console.log(response.data);

        if (payment.length > 0) {
          const filteredFundingPayment = {
            accountBalance: payment[0]?.transactionAfterBalance,
            transactionHistory: payment.map((history) => {
              const formattedDateTime = `${history.transactionDateTime.slice(2)}`;

              return {
                ...history,
                transactionDateTime: formattedDateTime,
              };
            }),
          };

          console.log('filteredFunding', filteredFundingPayment);
          setFilteredPayment(filteredFundingPayment);
        } else {
          setFilteredPayment({ accountBalance: 0, transactionHistory: [] });
        }
      } catch (error) {
        console.error(error);
        setError(true);
      }
    };

    fetchPayment();
  }, [fundingId]);

  return (
    <div className="funding-payment-container">
      <div className="funding-payment-balance">
        <img src={DefaultImage} alt="" className="funding-payment-image" />
        <div>
          <div>{artistName} 펀딩 계좌 잔액</div>
          <div>{filteredPayment.accountBalance.toLocaleString()}</div>
        </div>
      </div>
      <div className="funding-payment-list">
        {filteredPayment.transactionHistory.map((history, idx) => (
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

      {error && <Error setError={setError} />}
    </div>
  );
}

FundingPayment.propTypes = {
  artistName: PropTypes.string.isRequired,
  artistProfileImage: PropTypes.string.isRequired,
};

export default FundingPayment;
