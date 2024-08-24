import PropTypes from 'prop-types';
import './ArtistFundingCard.css';

// temp img
import temp from '../../assets/character/Sol.png';

function ArtistFundingCard({ funding, onClick }) {
  return (
    <div className="artist-funding-card" onClick={onClick}>
      <img src={temp} alt="img" className="artist-funding-img" />
      <div className="artist-funding-artist">{funding.artist}</div>
      <div className="artist-funding-title">{funding.title}</div>
      <div className="artist-funding-achievement">{funding.achievement}</div>
    </div>
  );
}

ArtistFundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired,
};

export default ArtistFundingCard;
