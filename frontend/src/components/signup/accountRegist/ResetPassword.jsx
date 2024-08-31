import './ResetPassword.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import AxiosInstance from '../../../util/AxiosInstance'; // AxiosInstance 임포트
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import SignUpButton from '../../common/SignUpButton';

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

  const handleNext = async () => {
    if (nextActive) {
      console.log(account);
      if (account.accountpasswordconfirm !== account.accountpassword) {
        // 비밀번호가 일치하지 않으면 경고를 띄우고 종료
        alert('비밀번호가 일치하지 않습니다.');
        handleClear();
        return;
      }

      try {
        // 비밀번호 확인을 위한 POST 요청
        const response = await AxiosInstance.post('/auth/signup', {
          email: account.email,
          password: account.password,
          name: account.name,
          nickname: account.nickname,
          birthDate: account.birthDate,
          phone: account.phone,
          profileImage: account.profileImage,
          accountNumber: account.accountNo,
          bankCode: '088',
          accountPassword: account.accountpassword,
          userKey: account.userKey,
        });
        console.log(response.data.status);
        if (response.data.status === 201) {
          // 성공 시 다음 단계로 이동
          navigate('/signup/created', { state: { account } });
        } else {
          // 실패 시 오류 메시지 표시하고 /login으로 이동
          alert('회원가입에 실패하셨습니다. 다시 시도해 주세요.');
          navigate('/signup/created', { state: { account } });
          navigate('/signup');
        }
      } catch (error) {
        console.error('비밀번호 재설정 요청 실패:', error);
        navigate('/signup/created', { state: { account } });
        alert('비밀번호 재설정 중 오류가 발생했습니다. 다시 시도해 주세요.');
        navigate('/login');
      }
    }
  };

  return (
    <>
      <div className="resetpass-container">
        <div className="resetpass-header">
          <SignUpButton />
          <div className="resetpass-header-description">비밀번호 재설정</div>
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
          회원가입 하기
        </WideButton>
      </div>
    </>
  );
}

export default ResetPassword;
