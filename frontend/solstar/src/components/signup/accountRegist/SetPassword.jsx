import './SetPassword.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';

function SetPassword() {
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
            비밀번호 설정
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div>
          입금자명의 4자리 숫자를
          <br />
          입력해 주세요.
        </div>

        <div>{account.accountNumber}</div>
        <div>{account.bankName}</div>
        <div>{account.accountHolder}</div>
        <form>
          <div>
            <label htmlFor="accountHolder">입금자명</label>
            <input
              type="text"
              id="accountHolder"
              value={account.accountHolder || ''}
              onChange={handleChange}
              placeholder="SOLSTAR 제외 4자리 숫자"
            />
            <img src={CircleX} alt="" />
          </div>
          <button>확인</button>
        </form>

        <WideButton
          isActive={nextActive}
          onClick={() => {
            navigate('/signup/reset', { state: { account } });
          }}
        >
          다음
        </WideButton>
      </div>
    </>
  );
}

export default SetPassword;
