import './BackButton.css';
import GoBack from '../../assets/common/GoBack.png';
import { useNavigate } from 'react-router-dom';

function BackButton() {
  const navigate = useNavigate();

  return (
    <img
      src={GoBack}
      alt=""
      className="go-back"
      onClick={() => {
        navigate(-1);
      }}
    />
  );
}

export default BackButton;
