import './WideButton.css';
import PropTypes from 'prop-types';

function WideButton({ children, isActive, onClick }) {
  return (
    <button
      onClick={onClick}
      className={`wide-button ${isActive ? 'active' : 'inactive'}`}
    >
      {children}
    </button>
  );
}

WideButton.propTypes = {
  isActive: PropTypes.bool,
  children: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
};

export default WideButton;
