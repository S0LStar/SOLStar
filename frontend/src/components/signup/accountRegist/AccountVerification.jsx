import './AccountVerification.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import AxiosInstance from '../../../util/AxiosInstance'; // AxiosInstance 임포트
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';
import SignUpButton from '../../common/SignUpButton';
import Error from '../../common/Error';

function AccountVerification() {
  const [error, setError] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(3);

  // 인증번호 설정
  const [account, setAccount] = useState({
    ...location.state.account,
    authCode: '',
  });
  const [nextActive, setNextActive] = useState(false);

  useEffect(() => {
    const isFormComplete = account.authCode.length === 4;
    setNextActive(isFormComplete);
    console.log(account);
  }, [account.authCode]);

  // 입력 시 변경
  const handleChange = (e) => {
    const value = e.target.value.replace(/[^0-9]/g, ''); // 숫자만 허용
    if (value.length <= 4) {
      setAccount((prevAccount) => ({
        ...prevAccount,
        authCode: value,
      }));
    }
  };

  const handleClear = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      authCode: '',
    }));
  };

  // 1원 송금 인증 버튼 클릭 시 실행될 함수
  const handleAuthCode = async () => {
    try {
      const response = await AxiosInstance.post(
        'auth/account/authenticate-check',
        {
          accountNo: account.accountNo,
          authCode: account.authCode,
          userKey: account.userKey,
        }
      );

      // 성공적으로 인증된 경우
      if (response.status === 200) {
        // 상태 코드 직접 확인
        // alert('인증이 완료되었습니다.');
        setError('인증이 완료되었습니다.');
        navigate('/signup/set', { state: { account } });
      } else {
        // 인증 실패 시 다른 인증 API 호출
        await sendAuthenticateRequest();
      }
    } catch (error) {
      // 첫 번째 요청이 실패한 경우에도 인증 API 호출
      await sendAuthenticateRequest();
    }
  };

  const sendAuthenticateRequest = async () => {
    try {
      const response = await AxiosInstance.post('/auth/account/authenticate', {
        accountNo: account.accountNo,
        userKey: account.userKey,
      });
      // alert('인증에 실패했습니다. 다시 시도해 주세요.');
      setError('인증에 실패했습니다. 다시 시도해 주세요.');
    } catch (error) {
      console.error('인증 중 오류가 발생했습니다. 다시 시도해 주세요.');
      // alert('인증 중 오류가 발생했습니다. 다시 시도해 주세요.');
      setError('인증 중 오류가 발생했습니다. 다시 시도해 주세요.');
    }
  };

  return (
    <>
      <div className="accountverify-container">
        <div className="accountverify-header">
          <SignUpButton />
          <div className="accountverify-header-description">1원 인증</div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="accountverify-description">
          입금자명의 4자리 숫자를 <br /> 입력해 주세요.
        </div>

        <div className="accountverify-bank-info">
          {account.bankName} {account.accountNo}
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
            <label htmlFor="authCode">입금자명</label>
            <div className="accountverify-input-wrapper">
              <input
                type="text"
                id="authCode"
                value={account.authCode}
                onChange={handleChange}
                placeholder="SOLSTAR 제외 4자리 숫자"
              />
              {account.authCode && (
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
              handleAuthCode();
            }
          }}
        >
          1원 송금 인증
        </WideButton>
      </div>

      {error && <Error setError={setError} />}
    </>
  );
}

export default AccountVerification;
