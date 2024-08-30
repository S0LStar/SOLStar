import { useState } from 'react';
import './FundingJoinPassword.css';
import PropTypes from 'prop-types';
import WideButton from '../../common/WideButton';
import axiosInstance from '../../../util/AxiosInstance';
import { useParams } from 'react-router-dom';

const FundingJoinPassword = ({ closeModal, totalJoinAmount, title }) => {
  const { fundingId } = useParams();
  const [password, setPassword] = useState('');
  const [shake, setShake] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleButtonClick = (value) => {
    if (password.length < 6) {
      setPassword((prev) => prev + value);
    }
  };

  const handleDelete = () => {
    setPassword(password.slice(0, -1));
  };

  const handleClear = () => {
    setPassword('');
  };

  const handleJoin = () => {
    console.log('펀딩 참여 비밀번호', password);

    // 펀딩 참여 API 연결
    const joinFunding = async () => {
      try {
        const response = await axiosInstance.post('/funding/join', {
          fundingId: parseInt(fundingId),
          amount: totalJoinAmount,
          password: password,
        });

        console.log(response);

        // 비밀번호가 맞은 경우, 후원 성공 modal (200)
        setSuccess(true);
      } catch (error) {
        if (error.status === 403) {
          // 비밀번호가 틀린 경우 (status 403)
          setShake(true);
          setTimeout(() => {
            setShake(false);
            setPassword('');
          }, 2000); // 애니메이션 시간과 동일하게 설정
        }
      }
    };

    joinFunding();
  };

  if (success) {
    return (
      <div className="password-modal-overlay" onClick={closeModal}>
        <div
          className="password-modal-content"
          onClick={(e) => e.stopPropagation()}
        >
          <h2>후원에 성공했습니다!</h2>
          <WideButton onClick={closeModal} isActive={true}>
            확인
          </WideButton>
        </div>
      </div>
    );
  }

  return (
    <div className="password-modal-overlay" onClick={closeModal}>
      <div
        className="password-modal-content"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="join-modal-pull-handle"></div>
        <div className="password-modal-title-container">
          <div className="password-modal-title">{title}</div>
          <div>펀딩으로</div>
        </div>
        <div className="password-modal-amount">
          {totalJoinAmount.toLocaleString()}원
        </div>
        {password.length === 0 ? (
          <p className="password-modal-instruction">
            맞으면 계좌 비밀번호를 눌러주세요
          </p>
        ) : (
          <>
            <div className={`password-dots ${shake ? 'shake' : ''}`}>
              {Array(6)
                .fill('')
                .map((_, index) => (
                  <div
                    key={index}
                    className={`dot ${index < password.length ? 'filled' : ''}`}
                  />
                ))}
            </div>
            {shake && (
              <div className="wrong-instruction">
                계좌 비밀번호를 잘못 입력하셨습니다
              </div>
            )}
          </>
        )}

        <div className="password-modal-keypad">
          <div className="password-modal-keypad-row">
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('1')}
            >
              1
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('2')}
            >
              2
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('3')}
            >
              3
            </button>
          </div>
          <div className="password-modal-keypad-row">
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('4')}
            >
              4
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('5')}
            >
              5
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('6')}
            >
              6
            </button>
          </div>
          <div className="password-modal-keypad-row">
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('7')}
            >
              7
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('8')}
            >
              8
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('9')}
            >
              9
            </button>
          </div>
          <div className="password-modal-keypad-row">
            <button
              className="password-modal-keypad-button all-delete"
              onClick={handleClear}
            >
              전체 삭제
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={() => handleButtonClick('0')}
            >
              0
            </button>
            <button
              className="password-modal-keypad-button"
              onClick={handleDelete}
            >
              ←
            </button>
          </div>
        </div>

        <WideButton onClick={handleJoin} isActive={password.length === 6}>
          후원
        </WideButton>
      </div>
    </div>
  );
};

FundingJoinPassword.propTypes = {
  closeModal: PropTypes.func.isRequired,
  totalJoinAmount: PropTypes.number.isRequired,
  title: PropTypes.string.isRequired,
};

export default FundingJoinPassword;
