import './CreatedAccount.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import SignUpButton from '../../common/SignUpButton';

function CreatedAccount() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(5);
  const account = location.state.account;

  return (
    <>
      <div className="created-container">
        <div className="created-header">
          <SignUpButton />
          <div className="created-header-description">내 참여 펀딩</div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="created-message">
          <h2>회원가입 완료</h2>
          <p>SOLSTAR를 가입해주셔서 감사합니다.</p>
        </div>

        <WideButton
          isActive={true}
          onClick={() => {
            navigate('/login');
          }}
          className="created-login-button"
        >
          로그인 하기
        </WideButton>
      </div>
    </>
  );
}

export default CreatedAccount;
