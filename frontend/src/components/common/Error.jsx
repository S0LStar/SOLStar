import { useNavigate } from 'react-router-dom';
import PropTypes from 'prop-types';
import Modal from './Modal';

function Error({ setError }) {
  const navigate = useNavigate();

  return (
    <Modal
      mainMessage="문제가 발생했습니다."
      subMessage="다시 시도해보세요."
      onClose={() => {
        setError(false);
        navigate(-1);
      }}
    />
  );
}

Error.propTypes = {
  setError: PropTypes.func.isRequired,
};
export default Error;
