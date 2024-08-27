import PropTypes from 'prop-types';
import temp from '../../../assets/character/Sol.png'; // 임시 이미지 경로
import './FundingCard.css';
import { useEffect } from 'react';

function FundingCard({ funding }) {
  useEffect(() => {
    console.log('FundingCard');
    console.log(funding);
  });

  return (
    <div className="search-result-funding-item">
      <img src={temp} alt="" className="search-result-funding-image" />
      <div className="search-result-funding-info">
        <span className="search-result-funding-artist">
          {funding.artistName}
        </span>
        <h3 className="search-result-funding-title">{funding.title}</h3>
        <div
          className={`search-result-funding-details 
            ${
              funding.status === 'PROCESSING'
                ? 'progress'
                : funding.status === 'SUCCESS' || funding.status === 'CLOSED'
                  ? 'success'
                  : 'fail'
            }`}
          style={{ '--achievement-percentage': funding.successRate }}
        >
          <div className="search-result-funding-detail">
            <span className="search-result-funding-progress">
              {funding.successRate}%
            </span>
            <span className="search-result-funding-amount">
              {funding.totalAmount.toLocaleString()}원
            </span>
            {funding.remainDays ? (
              <span className="search-result-funding-days-left">
                {funding.remainDays}일 남음
              </span>
            ) : funding.status === 'SUCCESS' ? (
              <span className="search-result-funding-days-left">펀딩 성공</span>
            ) : (
              <span className="search-result-funding-days-left">펀딩 무산</span>
            )}
          </div>
          <div className="popular-funding-achievement-bar"></div>
        </div>
      </div>
    </div>
  );
}

FundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
};

export default FundingCard;
