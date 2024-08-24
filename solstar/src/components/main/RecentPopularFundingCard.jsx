import PropTypes from 'prop-types';

function RecentPopularFundingCard({ funding, onClick }) {
  return (
    <>
      <div onClick={onClick}>{funding.title}</div>
    </>
  );
}

RecentPopularFundingCard.propTypes = {
  funding: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired,
};

export default RecentPopularFundingCard;
