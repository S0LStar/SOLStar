import PropTypes from 'prop-types';
import './ArtistFundingCard.css';

// temp img
import temp from '../../assets/character/Sol.png';
import Certification from '../../assets/common/Certification.png';

function ArtistFundingCard({ funding, onClick }) {
  return (
    <div className="artist-funding-card" onClick={onClick}>
      <div className="artist-funding-img-container">
        <img src={temp} alt="img" className="artist-funding-img" />
        {funding.isCertification && (
          <img src={Certification} className="certification-mark"></img>
        )}
      </div>
      <div className="artist-funding-artist">{funding.artist}</div>
      <div className="artist-funding-title">{funding.title}</div>
      {funding.status === 0 ? (
        <div className="artist-funding-achievement progress">
          {funding.achievement}%<div className="achievement-badge">진행 중</div>
        </div>
      ) : funding.status === 1 ? (
        <div className="artist-funding-achievement success">
          {funding.achievement}%
          <div className="achievement-badge">펀딩 성공</div>
        </div>
      ) : (
        <div className="artist-funding-achievement fail">
          {funding.achievement}%
          <div className="achievement-badge">펀딩 무산</div>
        </div>
      )}
    </div>
  );
}

ArtistFundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired,
};

export default ArtistFundingCard;
