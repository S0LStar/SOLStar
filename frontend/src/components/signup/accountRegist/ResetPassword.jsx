import './ResetPassword.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';

function ResetPassword() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(4);
  const account = location.state?.account || {};

  const [password, setPassword] = useState('');
  const [nextActive, setNextActive] = useState(false);

  // Check if password is set to enable the next button
  useEffect(() => {
    setNextActive(password.length === 6); // Enable 'Next' button when 6 characters are entered
  }, [password]);

  // Handle number button clicks
  const handleNumberClick = (num) => {
    if (password.length < 6) {
      setPassword(password + num);
    }
  };

  // Handle clear action
  const handleClear = () => {
    setPassword('');
  };

  // Handle backspace action
  const handleBackspace = () => {
    setPassword(password.slice(0, -1));
  };

  return (
    <>
      <div className="resetpass-container">
        <div className="resetpass-header">
          <div className="resetpass-header-backInfo">
            <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />
            비밀번호 확인
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="resetpass-instruction">비밀번호 입력</div>

        <div className="resetpass-input-indicator">
          {[...Array(6)].map((_, index) => (
            <div
              key={index}
              className={`circle ${password.length > index ? 'active' : ''}`}
            />
          ))}
        </div>

        <div className="keypad">
          {[1, 2, 3, 4, 5, 6, 7, 8, 9, '전체 삭제', 0, '←'].map(
            (item, index) => (
              <button
                key={index}
                className={`keypad-button ${
                  item === '전체 삭제' ? 'small-text' : ''
                }`}
                onClick={() => {
                  if (item === '전체 삭제') {
                    handleClear();
                  } else if (item === '←') {
                    handleBackspace();
                  } else {
                    handleNumberClick(item);
                  }
                }}
              >
                {item}
              </button>
            )
          )}
        </div>

        <WideButton
          isActive={nextActive}
          onClick={() => {
            if (nextActive) {
              navigate('/signup/created', { state: { account, password } });
            }
          }}
          className="resetpass-next-button"
        >
          다음
        </WideButton>
      </div>
    </>
  );
}

export default ResetPassword;
