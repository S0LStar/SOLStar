import './AccountRegistration.css';
import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';
import DownVector from '../../../assets/common/DownVector.png';

// Dummy icons for banks
const bankIcons = {
  신한은행: '/src/assets/signup/신한.png',
  국민은행: '/src/assets/signup/국민.png',
  우리은행: '/src/assets/signup/우리.png',
  하나은행: '/src/assets/signup/하나.png',
  카카오뱅크: '/src/assets/signup/카카오뱅크.png',
  토스: '/src/assets/signup/토스뱅크.png',
  기업은행: '/src/assets/signup/IBK기업.png',
  농협: '/src/assets/signup/NH농협.png',
  케이뱅크: '/src/assets/signup/케이뱅크.png',
  한국시티: '/src/assets/signup/씨티.png',
  SC제일: '/src/assets/signup/SC제일.png',
  경남: '/src/assets/signup/경남.png',
  광주: '/src/assets/signup/광주.png',
  전북: '/src/assets/signup/전북.png',
  부산: '/src/assets/signup/부산.png',
  대구: '/src/assets/signup/대구.png',
  우체국: '/src/assets/signup/우체국.png',
  새마을: '/src/assets/signup/새마을.png',
  수협: '/src/assets/signup/수협.png',
  신협: '/src/assets/signup/신협.png',
  KDB산업: '/src/assets/signup/KDB산업.png',
};

function AccountRegistration() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentStep] = useState(3);
  const [account, setAccount] = useState({
    ...location.state?.account,
    accountNumber: '',
    bankName: '',
    accountHolder: '',
  });
  const [nextActive, setNextActive] = useState(false);
  const [showModal, setShowModal] = useState(false);

  // Check if all fields are filled
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

  const handleClearAccountNumber = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountNumber: '',
      accountHolder: '',
    }));
  };

  // Show bank selection modal
  const handleShowModal = () => {
    setShowModal(true);
  };

  // Select bank from modal
  const handleSelectBank = (bankName) => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      bankName,
      accountHolder: '',
    }));
    setShowModal(false);
  };

  const handleConfirm = () => {
    if (account.accountNumber && account.bankName) {
      setTimeout(() => {
        setAccount((prevAccount) => ({
          ...prevAccount,
          accountHolder: '홍길동',
        }));
      }, 1000);
    } else {
      alert('계좌번호와 은행명을 입력해 주세요.');
    }
  };

  return (
    <>
      <div className="accountregist-container">
        <div className="accountregist-header">
          <div className="accountregist-header-backInfo">
            <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />
            1원 인증
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="accountregist-description">
          본인 명의 은행 계좌를
          <br />
          입력해 주세요.
        </div>
        <form className="accountregist-form">
          <div className="accountregist-form-field">
            <label htmlFor="accountNumber">계좌번호</label>
            <div>
              <input
                type="text"
                id="accountNumber"
                value={account.accountNumber}
                onChange={handleChange}
                placeholder="'-'없이 계좌번호 입력"
              />
              <img
                src={CircleX}
                alt="계좌번호 입력 지우기"
                className="clear-icon"
                onClick={handleClearAccountNumber}
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
    </>
  );
}

export default AccountRegistration;
