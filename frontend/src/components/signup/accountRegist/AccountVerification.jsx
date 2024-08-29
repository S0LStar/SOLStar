import './AccountVerification.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';

function AccountVerification() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(3);

  // 인증번호 설정
  const [account, setAccount] = useState({
    ...location.state.account,
    verificationCode: '',
  });
  const [nextActive, setNextActive] = useState(false);

  useEffect(() => {
    const isFormComplete = account.verificationCode.length === 4;
    setNextActive(isFormComplete);
  }, [account.verificationCode]);

  // 입력 시 변경
  const handleChange = (e) => {
    const value = e.target.value.replace(/[^0-9]/g, ''); // Only allow numeric input
    if (value.length <= 4) {
      setAccount((prevAccount) => ({
        ...prevAccount,
        verificationCode: value,
      }));
    }
  };

  const handleClear = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      verificationCode: '',
    }));
  };

  return (
    <>
      <div className="accountverify-container">
        <div className="accountverify-header">
          <div className="accountverify-header-backInfo">
            <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />
            1원 인증
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="accountverify-description">
          입금자명의 4자리 숫자를 <br /> 입력해 주세요.
        </div>

        <div className="accountverify-bank-info">
          {account.bankName} {account.accountNumber}
        </div>

        <div className="accountverify-info-box">
          <div className="accountverify-info-header">
            <span>입금자명</span>
          </div>
          <div className="accountverify-info-content">
            <span>SOLSTAR</span>
            <div className="accountverify-digits">
              {[...Array(4)].map((_, index) => (
                <div key={index} className="digit-box">
                  5
                </div>
              ))}
            </div>
            <span>1원</span>
          </div>
          <div className="accountverify-info-footer">
            계좌로 입금된 1원의 입금자명의 숫자를 확인해 주세요
          </div>
        </div>

        <form className="accountverify-form">
          <div className="accountverify-form-field">
            <label htmlFor="verificationCode">입금자명</label>
            <div className="accountverify-input-wrapper">
              <input
                type="text"
                id="verificationCode"
                value={account.verificationCode} // Use account.verificationCode directly
                onChange={handleChange}
                placeholder="SOLSTAR 제외 4자리 숫자"
              />
              {account.verificationCode && (
                <img
                  src={CircleX}
                  alt="입력 지우기"
                  className="clear-icon"
                  onClick={handleClear}
                />
              )}
            </div>
            <hr />
          </div>
        </form>

        <WideButton
          isActive={nextActive}
          onClick={() => {
            if (nextActive) {
              navigate('/signup/set', { state: { account } });
            }
          }}
        >
          1원 송금 인증
        </WideButton>
      </div>
    </>
  );
}

export default AccountVerification;
