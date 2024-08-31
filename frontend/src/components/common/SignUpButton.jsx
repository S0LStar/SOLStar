import './SignUpButton.css';
import GoBack from '../../assets/common/GoBack.png';
import { useNavigate } from 'react-router-dom';

function SignUpButton() {
  const navigate = useNavigate();

  return (
    <img
      src={GoBack}
      alt=""
      className="go-back"
      onClick={() => {
        navigate('/login');
      }}
    />
  );
}

export default SignUpButton;
