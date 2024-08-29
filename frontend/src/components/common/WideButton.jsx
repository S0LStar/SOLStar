import './WideButton.css';
import PropTypes from 'prop-types';

function WideButton({ children, isActive, onClick }) {
  return (
    <div className="wide-button-container">
      <button
        onClick={onClick}
        className={`wide-button ${isActive ? 'active' : 'inactive'}`}
        disabled={!isActive && true}
      >
        {children}
      </button>
    </div>
  );
}

WideButton.propTypes = {
  isActive: PropTypes.bool,
  children: PropTypes.string.isRequired,
  onClick: PropTypes.func.isRequired,
};

export default WideButton;
