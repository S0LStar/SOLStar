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

  if (!isOpen) return null; // 모달이 열려있지 않으면 렌더링하지 않음

  return (
    <div className="join-modal-overlay" onClick={closeModal}>
      <div className="join-modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="join-modal-pull-handle"></div>
        <h2>펀딩 참여 금액</h2>
        <div className="join-modal-amount-buttons">
          <button className="join-modal-amount-button">+ 5,000 원</button>
          <button className="join-modal-amount-button">+ 10,000 원</button>
          <button className="join-modal-amount-button">+ 50,000 원</button>
        </div>
        <div className="join-modal-current-amount">
          <div>{totalJoinAmount}</div>
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
  closeModal: PropTypes.bool.isRequired,
};

export default FundingJoinModal;
