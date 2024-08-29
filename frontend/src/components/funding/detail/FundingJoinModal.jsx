import { useEffect, useState } from 'react';
import './FundingJoinModal.css';
import PropTypes from 'prop-types';
import WideButton from '../../common/WideButton';
import JoinPassword from './FundingJoinPassword';

const FundingJoinModal = ({ isOpen, closeModal, title }) => {
  const [totalJoinAmount, setTotalAmount] = useState(0);
  const [JoinPasswordModal, setJoinPasswordModal] = useState(false);

  useEffect(() => {
    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'unset';
    }

    // 컴포넌트가 언마운트될 때 overflow를 원래대로 돌려놓음
    return () => {
      document.body.style.overflow = 'unset';
    };
  }, [isOpen]);

  // 사용자가 금액 입력시에, 숫자만 입력 받음
  const handleJoinAmount = (event) => {
    const value = event.target.value;

    if (/^\d*$/.test(value)) {
      setTotalAmount(value);
    }
  };

  // 금액 추가 버튼 클릭 시 이전 금액에 합
  const handleAmountButtonClick = (amount) => {
    setTotalAmount((prevTotalAmount) => {
      const newTotal = parseInt(prevTotalAmount || '0', 10) + amount;
      return newTotal.toString();
    });
  };

  // input에 focus 시에 총액이 0이면, 빈문자열로 초기화
  const handleFocus = () => {
    if (totalJoinAmount === 0) {
      setTotalAmount('');
    }
  };

  const handleBlur = () => {
    if (totalJoinAmount === '') {
      setTotalAmount('0');
    }
  };

  const handleJoin = () => {
    console.log(totalJoinAmount);
    setJoinPasswordModal(true);
  };

  return (
    <div className="join-modal-overlay" onClick={closeModal}>
      {!JoinPasswordModal && (
        <div
          className="join-modal-content"
          onClick={(e) => e.stopPropagation()}
        >
          <div className="join-modal-pull-handle"></div>
          <h2>펀딩 참여 금액</h2>
          <div className="join-modal-amount-buttons">
            <button
              className="join-modal-amount-button"
              onClick={() => handleAmountButtonClick(5000)}
            >
              + 5,000 원
            </button>
            <button
              className="join-modal-amount-button"
              onClick={() => handleAmountButtonClick(10000)}
            >
              + 10,000 원
            </button>
            <button
              className="join-modal-amount-button"
              onClick={() => handleAmountButtonClick(50000)}
            >
              + 50,000 원
            </button>
          </div>
          <div className="join-modal-current-amount">
            <input
              type="number"
              value={totalJoinAmount}
              onChange={handleJoinAmount}
              inputMode="numeric"
              onFocus={handleFocus}
              onBlur={handleBlur}
              pattern="[0-9]*"
              className="join-modal-current-amount-input"
            />
            원
          </div>
          <WideButton onClick={handleJoin} isActive={totalJoinAmount > 0}>
            펀딩 후원하기
          </WideButton>
        </div>
      )}
      {JoinPasswordModal && (
        <JoinPassword
          closeModal={closeModal}
          totalJoinAmount={parseInt(totalJoinAmount, 10)}
          title={title}
        />
      )}
    </div>
  );
};

FundingJoinModal.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  closeModal: PropTypes.func.isRequired,
  title: PropTypes.string.isRequired,
};

export default FundingJoinModal;
