import './Loading.css';
import PropTypes from 'prop-types';

const Loading = ({ children }) => {
  return (
    <div className="loading-overlay">
      <div className="loading-content">
        {children && <div className="loading-text">{children}</div>}
        <div className="loading-subtext">잠시만 기다려주세요!</div>
        <div className="loader-wrapper">
          <div className="loader"></div>
        </div>
      </div>
    </div>
  );
};

Loading.propTypes = {
  children: PropTypes.string,
};

export default Loading;
