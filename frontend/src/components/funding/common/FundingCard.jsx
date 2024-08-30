import PropTypes from 'prop-types';
import temp from '../../../assets/character/Sol.png'; // 임시 이미지 경로
import './FundingCard.css';
import { useEffect, useState } from 'react';
import Certification from '../../../assets/common/Certification.png';
import { useNavigate } from 'react-router-dom';

function FundingCard({ funding }) {
  const navigate = useNavigate();
  const [successRate, setSuccessRate] = useState(0);

  useEffect(() => {
    console.log('FundingCard');
    console.log(funding);

    // 펀딩 달성률이 없을 시에는 계산
    if (!funding.successRate) {
      setSuccessRate(
        Math.floor((funding.totalAmount * 100) / funding.goalAmount)
      );
    }
  });

  return (
    <div
      className="funding-card-item"
      onClick={() => navigate(`/funding/${funding.fundingId}`)}
    >
      <div className="funding-card-image-container">
        <img src={temp} alt="img" className="funding-card-image" />
        {funding.type === 'VERIFIED' && (
          <img src={Certification} className="certification-mark"></img>
        )}
      </div>
      <div className="funding-card-info">
        <div>
          <span className="funding-card-artist">{funding.artistName}</span>
          <h3 className="funding-card-title">{funding.title}</h3>
        </div>
        <div
          className={`funding-card-details 
            ${
              funding.status === 'PROCESSING'
                ? 'progress'
                : funding.status === 'SUCCESS' || funding.status === 'CLOSED'
                  ? 'success'
                  : 'fail'
            }`}
          style={{ '--achievement-percentage': funding.successRate }}
        >
          <div className="funding-card-detail">
            <span className="funding-card-progress">
              {funding.successRate || successRate}%
            </span>
            <span className="funding-card-amount">
              {funding.totalAmount.toLocaleString()}원
            </span>
            {funding.status === 'PROCESSING' ? (
              <span className="funding-card-days-left">
                {funding.remainDays}일 남음
              </span>
            ) : funding.status === 'SUCCESS' || funding.status === 'CLOSED' ? (
              <span className="funding-card-days-left">펀딩 성공</span>
            ) : (
              <span className="funding-card-days-left">펀딩 무산</span>
            )}
          </div>
          <div className="funding-card-achievement-bar"></div>
        </div>
      </div>
    </div>
  );
}

FundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
};

export default FundingCard;
