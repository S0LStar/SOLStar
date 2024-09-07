import './AccountRegistration.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import AxiosInstance from '../../../util/AxiosInstance'; // AxiosInstance 임포트
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';
import DownVector from '../../../assets/common/DownVector.png';
import axiosInstance from '../../../util/AxiosInstance';
import SignUpButton from '../../common/SignUpButton';
import Error from '../../common/Error';

// 은행별 modal 아이콘
const bankIcons = {
  신한은행: '/src/assets/signup/Shin.png',
  국민은행: '/src/assets/signup/Kook.png',
  우리은행: '/src/assets/signup/Woori.png',
  하나은행: '/src/assets/signup/Ha.png',
  카카오뱅크: '/src/assets/signup/Kakko.png',
  토스: '/src/assets/signup/Toss.png',
  기업은행: '/src/assets/signup/Sc.png',
  농협: '/src/assets/signup/NH.png',
  케이뱅크: '/src/assets/signup/K.png',
  한국시티: '/src/assets/signup/City.png',
  SC제일: '/src/assets/signup/Sc.png',
  경남: '/src/assets/signup/Kyoun.png',
  광주: '/src/assets/signup/Hwang.png',
  전북: '/src/assets/signup/Jeon.png',
  부산: '/src/assets/signup/Bu.png',
  대구: '/src/assets/signup/Dae.png',
  우체국: '/src/assets/signup/Woo.png',
  새마을: '/src/assets/signup/Sae.png',
  수협: '/src/assets/signup/Soo.png',
  신협: '/src/assets/signup/Shin.png',
  KDB산업: '/src/assets/signup/Kdb.png',
};

function AccountRegistration() {
  const [error, setError] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(3);
  const [account, setAccount] = useState({
    ...location.state.account,
    accountNo: '',
    bankName: '',
    accountHolder: '',
    userKey: '',
  });
  const [nextActive, setNextActive] = useState(false);
  const [showModal, setShowModal] = useState(false);

  // 페이지 로딩 시 실행할 axios 통신
  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        const response = await AxiosInstance.post(
          'auth/user-account/validate',
          // { userId: 'ksh219805@gmail.com' }
          { userId: account.email }
        );
        console.log('초기 데이터:', response.data);

        // userKey를 account 상태에 추가로 저장
        if (response.data && response.data.data) {
          setAccount((prevAccount) => ({
            ...prevAccount,
            userKey: response.data.data.useKey, // 올바른 key 명 확인
          }));
        }
      } catch (error) {
        console.error('초기 데이터 불러오기 실패:', error);
      }
    };
    fetchInitialData(); // 컴포넌트 마운트 시 실행
  }, []); // 빈 배열: 처음 마운트될 때만 실행

  // 값 다 채워졌는지 확인
  useEffect(() => {
    const isFormComplete = Boolean(
      account.accountNo && account.bankName && account.accountHolder
    );

    setNextActive(isFormComplete);
  }, [account]);

  // 값 변경 시 입력
  const handleChange = (e) => {
    const { id, value } = e.target;
    setAccount((prevAccount) => ({
      ...prevAccount,
      [id]: value,
    }));
  };

  const handleClearAccountNo = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountNo: '',
      accountHolder: '',
    }));
  };

  // 모달
  const handleShowModal = () => {
    setShowModal(true);
  };

  // 모달에서 은행 선택
  const handleSelectBank = (bankName) => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      bankName,
      accountHolder: '',
    }));
    setShowModal(false);
  };

  const handleConfirm = async () => {
    if (account.accountNo) {
      try {
        const response = await axiosInstance.post(
          '/auth/account/authenticate',
          {
            accountNo: account.accountNo,
            userKey: account.userKey,
          }
        );

        // 예금주명 (accountHolder) 업데이트
        setAccount((prevAccount) => ({
          ...prevAccount,
          accountHolder: account.name, // 예금주명을 서버에서 반환한다면 사용하고, 그렇지 않다면 기본값 사용
        }));

        // alert('계좌 인증이 성공적으로 완료되었습니다.');
        setError('계좌 인증이 성공적으로 완료되었습니다.');
      } catch (error) {
        console.error('계좌 인증 실패:', error);
        // 계좌 인증 실패 메시지 출력
        // alert('계좌 인증에 실패했습니다. 다시 시도해 주세요.');
        setError('계좌 인증에 실패했습니다. 다시 시도해 주세요.');
      }
    } else {
      // alert('계좌번호와 은행명을 입력해 주세요.');
      setError('계좌번호와 은행명을 입력해 주세요.');
    }
  };

  return (
    <>
      <div className="accountregist-container">
        <div className="accountregist-header">
          <SignUpButton />
          <div className="accountregist-header-description">1원 요청</div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="accountregist-description">
          본인 명의 은행 계좌를
          <br />
          입력해 주세요.
        </div>
        <form className="accountregist-form">
          <div className="accountregist-form-field">
            <label htmlFor="accountNo">계좌번호</label>
            <div>
              <input
                type="text"
                id="accountNo"
                value={account.accountNo}
                onChange={handleChange}
                placeholder="'-'없이 계좌번호 입력"
              />
              <img
                src={CircleX}
                alt="계좌번호 입력 지우기"
                className="clear-icon"
                onClick={handleClearAccountNo}
              />
            </div>
            <hr />
          </div>
          <div className="accountregist-form-field">
            <label htmlFor="bankName">은행명</label>
            <div>
              <input
                type="text"
                id="bankName"
                value={account.bankName}
                onChange={handleChange}
                placeholder="은행 선택"
                readOnly
              />
              <img
                src={DownVector}
                alt="은행 선택"
                className="dropdown-icon"
                onClick={handleShowModal}
              />
            </div>
            <hr />
          </div>
          <div className="accountregist-form-field">
            <label htmlFor="accountHolder">예금주명</label>
            <div>
              <input
                type="text"
                id="accountHolder"
                value={account.accountHolder}
                onChange={handleChange}
                placeholder="예금주명"
                readOnly
              />
              <button
                type="button"
                className="accountregist-confirm-button"
                onClick={handleConfirm}
              >
                확인
              </button>
            </div>
            <hr />
          </div>
        </form>

        {showModal && (
          <div className="bank-modal">
            <div className="bank-modal-content">
              <div className="bank-modal-header">
                <span>
                  <h3>은행 선택</h3>
                </span>
                <span
                  className="close-modal"
                  onClick={() => setShowModal(false)}
                >
                  닫기
                </span>
              </div>
              <div className="bank-grid">
                {Object.keys(bankIcons).map((bankName) => (
                  <div
                    key={bankName}
                    className="bank-item"
                    onClick={() => handleSelectBank(bankName)}
                  >
                    <img src={bankIcons[bankName]} alt={bankName} />
                    <span>{bankName}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        <WideButton
          isActive={nextActive}
          onClick={() => {
            navigate('/signup/verify', { state: { account } });
          }}
        >
          1원 송금 요청
        </WideButton>
      </div>

      {error && <Error setError={setError} />}
    </>
  );
}

export default AccountRegistration;
