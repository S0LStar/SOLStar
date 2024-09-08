import PropTypes from 'prop-types';

import temp from '../../assets/character/Shoo.png';
import Certification from '../../assets/common/Certification.png';
import './RecentPopularFundingCard.css';

function RecentPopularFundingCard({ funding, onClick, index }) {
  return (
    <div className="popular-funding-card" onClick={onClick}>
      <div className="popular-funding-card-index">{index + 1}</div>
      <div className="popular-funding-img-container">
        <img
          src={funding.fundingImage}
          alt="img"
          className="popular-funding-img"
        />
        {funding.type === 'VERIFIED' && (
          <img src={Certification} className="certification-mark"></img>
        )}
      </div>
      <div className="popular-funding-content">
        <header className="popular-funding-content-header">
          <div className="popular-funding-artist">{funding.artistName}</div>
        </header>
        <div className="popular-funding-title">{funding.title}</div>
        <div
          className={`popular-funding-achievement-container 
            ${
              funding.status === 'PROCESSING'
                ? 'progress'
                : funding.status === 'SUCCESS' || funding.status === 'CLOSED'
                  ? 'success'
                  : 'fail'
            }`}
          style={{ '--achievement-percentage': funding.successRate }}
        >
          <div className="popular-funding-achievement">
            <div className="popular-funding-achievement-percent">
              {funding.successRate}%
            </div>
            <div className="popular-funding-amount">
              {funding.totalAmount.toLocaleString()}원
            </div>
          </div>
          <div className="popular-funding-achievement-bar"></div>
        </div>
      </div>
    </div>
  );
}

RecentPopularFundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired,
  index: PropTypes.number.isRequired,
};

export default RecentPopularFundingCard;
