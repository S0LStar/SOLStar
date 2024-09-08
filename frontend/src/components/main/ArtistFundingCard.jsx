import PropTypes from 'prop-types';
import './ArtistFundingCard.css';

// temp img
import temp from '../../assets/character/Sol.png';
import Certification from '../../assets/common/Certification.png';

function ArtistFundingCard({ funding, onClick }) {
  return (
    <div className="artist-funding-card" onClick={onClick}>
      <div className="artist-funding-img-container">
        <img
          src={funding.fundingImage}
          alt="img"
          className="artist-funding-img"
        />
        {funding.type === 'VERIFIED' && (
          <img src={Certification} className="certification-mark"></img>
        )}
      </div>
      <div className="artist-funding-artist">{funding.artistName}</div>
      <div className="artist-funding-title">{funding.title}</div>
      {funding.type !== 'ADVERTISE' && (
        <div className="artist-funding-achievement progress">
          {funding.successRate}% 달성
        </div>
      )}
      {funding.type === 'ADVERTISE' && (
        <div className="advertise-badge">홍보글</div>
      )}
    </div>
  );
}

ArtistFundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired,
};

export default ArtistFundingCard;
