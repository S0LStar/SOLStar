import PropTypes from 'prop-types';

import temp from '../../assets/character/Shoo.png';
import Certification from '../../assets/common/Certification.png';
import './RecentPopularFundingCard.css';

function RecentPopularFundingCard({ funding, onClick, index }) {
  return (
    <div className="popular-funding-card" onClick={onClick}>
      <div className="popular-funding-card-index">{index + 1}</div>
      <div className="popular-funding-img-container">
        <img src={temp} alt="img" className="popular-funding-img" />
        {funding.isCertification && (
          <img src={Certification} className="certification-mark"></img>
        )}
      </div>
      <div className="popular-funding-content">
        <div className="popular-funding-artist">{funding.artist}</div>
        <div className="popular-funding-title">{funding.title}</div>
        {funding.status === 0 ? (
          <div className="popular-funding-achievement progress">
            {funding.achievement}%
            <div className="achievement-badge">진행 중</div>
          </div>
        ) : funding.status === 1 ? (
          <div className="popular-funding-achievement success">
            {funding.achievement}%
            <div className="achievement-badge">펀딩 성공</div>
          </div>
        ) : (
          <div className="popular-funding-achievement fail">
            {funding.achievement}%
            <div className="achievement-badge">펀딩 무산</div>
          </div>
        )}
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
