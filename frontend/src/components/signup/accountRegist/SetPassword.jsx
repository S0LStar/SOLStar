import './SetPassword.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import SignUpButton from '../../common/SignUpButton';

function SetPassword() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(4);

  // 초기 계정 정보 입력
  const [account, setAccount] = useState({
    ...location.state?.account,
    accountpassword: '',
  });
  const [nextActive, setNextActive] = useState(false);

  // 비밀번호 6자리 확인
  useEffect(() => {
    setNextActive(account.accountpassword.length === 6); // Enable 'Next' button when 6 characters are entered
  }, [account.accountpassword]);

  // 비밀번호 입력
  const handleNumberClick = (num) => {
    if (account.accountpassword.length < 6) {
      setAccount((prevAccount) => ({
        ...prevAccount,
        accountpassword: prevAccount.accountpassword + num,
      }));
    }
  };

  // 초기화 버튼
  const handleClear = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountpassword: '',
    }));
  };

  // 1글자 지우기 버튼
  const handleBackspace = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountpassword: prevAccount.accountpassword.slice(0, -1),
    }));
  };

  const handleNext = () => {
    if (nextActive) {
      navigate('/signup/reset', { state: { account } });
    }
  };

  return (
    <>
      <div className="setpass-container">
        <div className="setpass-header">
          <SignUpButton />
          <div className="setpass-header-description">비밀번호 설정</div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="setpass-instruction">비밀번호 입력</div>

        <div className="setpass-input-indicator">
          {[...Array(6)].map((_, index) => (
            <div
              key={index}
              className={`circle ${account.accountpassword.length > index ? 'active' : ''}`}
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
          onClick={handleNext} // Use handleNext to navigate
          className="setpass-next-button"
        >
          다음
        </WideButton>
      </div>
    </>
  );
}

export default SetPassword;
