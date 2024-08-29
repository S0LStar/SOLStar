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

  // 초기 계정 정보 입력
  const [account, setAccount] = useState({
    ...location.state.account,
    accountpasswordconfirm: '',
  });
  const [nextActive, setNextActive] = useState(false);

  // 비밀번호 6자리 확인
  useEffect(() => {
    setNextActive(account.accountpasswordconfirm.length === 6);
  }, [account.accountpasswordconfirm]);

  const handleNumberClick = (num) => {
    if (account.accountpasswordconfirm.length < 6) {
      setAccount((prevAccount) => ({
        ...prevAccount,
        accountpasswordconfirm: prevAccount.accountpasswordconfirm + num,
      }));
    }
  };

  // 초기화 버튼
  const handleClear = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountpasswordconfirm: '',
    }));
  };

  // 1글자 지우기 버튼
  const handleBackspace = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountpasswordconfirm: prevAccount.accountpasswordconfirm.slice(0, -1),
    }));
  };

  const handleNext = () => {
    if (nextActive) {
      if (account.accountpasswordconfirm === account.accountpassword) {
        navigate('/signup/created', { state: { account } });
      } else {
        alert('비밀번호가 일치하지 않습니다.'); // Show alert if passwords do not match
      }
    }
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
              className={`circle ${
                account.accountpasswordconfirm.length > index ? 'active' : ''
              }`}
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
          className="resetpass-next-button"
        >
          다음
        </WideButton>
      </div>
    </>
  );
}

export default ResetPassword;
