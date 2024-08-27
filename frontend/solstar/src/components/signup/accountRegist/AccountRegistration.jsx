import './AccountRegistration.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';
import DownVector from '../../../assets/common/DownVector.png';

function AccountRegistration() {
  const navigate = useNavigate();
  const [currentStep] = useState(3);
  const [account, setAccount] = useState({
    accountNumber: '',
    bankName: '',
    accountHolder: '',
  });
  const [nextActive, setNextActive] = useState(false); // 다음 버튼 활성화 상태

  // 모든 필드가 채워졌는지 확인
  useEffect(() => {
    const isFormComplete = Boolean(
      account.accountNumber && account.bankName && account.accountHolder
    );

    setNextActive(isFormComplete);
  }, [account]);

  // Input change handler
  const handleChange = (e) => {
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
            1원 인증
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div>
          본인 명의 은행 계좌를
          <br />
          입력해 주세요.
        </div>
        <form>
          <div>
            <label htmlFor="accountNumber">계좌번호</label>
            <input
              type="text"
              id="accountNumber"
              value={account.accountNumber}
              onChange={handleChange}
              placeholder="'-'없이 계좌번호 입력"
            />
            <img src={CircleX} alt="" />
          </div>
          <div>
            <label htmlFor="bankName">은행명</label>
            <input
              type="text"
              id="bankName"
              value={account.bankName}
              onChange={handleChange}
              placeholder="은행 선택"
            />
            <img src={DownVector} alt="" />
          </div>
          <div>
            <label htmlFor="accountHolder">예금주명</label>
            <input
              type="text"
              id="accountHolder"
              value={account.accountHolder}
              onChange={handleChange}
              placeholder="예금주명"
            />
          </div>
          <button>확인</button>
        </form>

        <WideButton
          isActive={nextActive}
          onClick={() => {
            navigate('/signup/verify', { state: { account } });
          }}
        >
          다음
        </WideButton>
      </div>
    </>
  );
}

export default AccountRegistration;
