import './AccountRegistration.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CircleX from '../../../assets/signup/CircleX.png';
import DownVector from '../../../assets/common/DownVector.png';

// Dummy icons for banks
const bankIcons = {
  국민은행: 'path/to/kookmin-icon.png',
  신한은행: 'path/to/shinhan-icon.png',
  우리은행: 'path/to/woori-icon.png',
  하나은행: 'path/to/hana-icon.png',
  카카오뱅크: 'path/to/kakao-icon.png',
  기업은행: 'path/to/ibk-icon.png',
  농협: 'path/to/nh-icon.png',
  케이뱅크: 'path/to/kbank-icon.png',
  한국시티: 'path/to/citi-icon.png',
  SC제일: 'path/to/sc-icon.png',
  경남: 'path/to/kyongnam-icon.png',
  광주: 'path/to/gwangju-icon.png',
};

function AccountRegistration() {
  const navigate = useNavigate();
  const [currentStep] = useState(3);
  const [account, setAccount] = useState({
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

  // Clear account number
  const handleClearAccountNumber = () => {
    setAccount((prevAccount) => ({
      ...prevAccount,
      accountNumber: '',
      accountHolder: '', // Clear account holder as well, if account number changes
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
      accountHolder: '', // Clear account holder to trigger re-verification
    }));
    setShowModal(false);
  };

  // Fetch account holder name on button click
  const handleConfirm = () => {
    if (account.accountNumber && account.bankName) {
      // Simulating an API call
      setTimeout(() => {
        setAccount((prevAccount) => ({
          ...prevAccount,
          accountHolder: '홍길동', // Example account holder name
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
              <h3>은행 선택</h3>
              <span className="close-modal" onClick={() => setShowModal(false)}>
                닫기
              </span>
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
