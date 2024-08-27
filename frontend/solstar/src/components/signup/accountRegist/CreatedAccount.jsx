import './CreatedAccount.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';

function CreatedAccount() {
  const navigate = useNavigate();
  const location = useLocation(); // Use useLocation to get the passed state
  const [currentStep] = useState(3);
  const account = location.state?.account || {}; // Retrieve the account data from location.state safely

  const [nextActive, setNextActive] = useState(false); // 다음 버튼 활성화 상태

  // 모든 필드가 채워졌는지 확인
  useEffect(() => {
    const isFormComplete = Boolean(account.accountHolder);
    setNextActive(isFormComplete);
  }, [account]);

  // Input change handler
  const handleChange = (e) => {
    // Assuming we need to handle changes within this component
    const { id, value } = e.target;
    setAccount((prevAccount) => ({
      ...prevAccount,
      [id]: value,
    }));
  };

  return (
    <>
      <div>
        <div>
          <div>
            <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />
            회원가입 완료
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div>회원가입 완료</div>
        <div>SOLSTAR를 가입해주셔서 감사합니다.</div>
        <WideButton
          isActive={true}
          onClick={() => {
            navigate('/signup/created');
          }}
        >
          로그인 하기
        </WideButton>
      </div>
    </>
  );
}

export default CreatedAccount;
