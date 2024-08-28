import { useEffect, useState } from 'react';
import './FundingJoinModal.css';
import PropTypes from 'prop-types';

const FundingJoinModal = ({ isOpen, closeModal }) => {
  const [totalJoinAmount, setTotalAmount] = useState(0);

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

  const handleJoinAmount = (event) => {
    const value = parseInt(event.target.value, 10);
    setTotalAmount(isNaN(value) ? 0 : value);
  };

  const handleAmountButtonClick = (amount) => {
    setTotalAmount((totalJoinAmount) => totalJoinAmount + amount);
  };

  return (
    <div className="join-modal-overlay" onClick={closeModal}>
      <div className="join-modal-content" onClick={(e) => e.stopPropagation()}>
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
          />
        </div>
        <button className="join-modal-join-button" onClick={closeModal}>
          펀딩 참여하기
        </button>
      </div>
    </div>
  );
};

FundingJoinModal.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  closeModal: PropTypes.func.isRequired,
};

export default FundingJoinModal;
