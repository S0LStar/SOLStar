import PropTypes from 'prop-types';
import './Empty.css';

function Empty({ children }) {
  return (
    <div className="empty-state">
      <span>{children}</span>이 없습니다.
      <div>직접 만들어 보는 건 어떠세요 ?</div>
    </div>
  );
}

Empty.propTypes = {
  children: PropTypes.string.isRequired,
};

export default Empty;
