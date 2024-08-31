import './TermOfService.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import CheckElipse from '../../../assets/signup/CheckElipse.png';
import Circle from '../../../assets/signup/Circle.png';
import Check from '../../../assets/signup/Check.png';
import ActiveCheck from '../../../assets/signup/ActiveCheck.png';
import SignUpButton from '../../common/SignUpButton';

function TermOfService() {
  const navigate = useNavigate();
  const [currentStep] = useState(1);
  const [allAgreed, setAllAgreed] = useState(false);
  const [personalInfoAgreed, setPersonalInfoAgreed] = useState(false);
  const [thirdPartyInfoAgreed, setThirdPartyInfoAgreed] = useState(false);

  const handleAllAgreeToggle = () => {
    const newAllAgreed = !allAgreed;
    setAllAgreed(newAllAgreed);
    setPersonalInfoAgreed(newAllAgreed);
    setThirdPartyInfoAgreed(newAllAgreed);
  };

  const handlePersonalInfoToggle = () => {
    const newPersonalInfoAgreed = !personalInfoAgreed;
    setPersonalInfoAgreed(newPersonalInfoAgreed);
    setAllAgreed(newPersonalInfoAgreed && thirdPartyInfoAgreed);
  };

  const handleThirdPartyInfoToggle = () => {
    const newThirdPartyInfoAgreed = !thirdPartyInfoAgreed;
    setThirdPartyInfoAgreed(newThirdPartyInfoAgreed);
    setAllAgreed(newThirdPartyInfoAgreed && personalInfoAgreed);
  };

  return (
    <>
      <div className="term-container">
        <div className="term-header">
          <SignUpButton />
          <div className="term-header-description">약관 동의</div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div className="term-main">
          <div onClick={handleAllAgreeToggle} className="agree-item">
            <div>
              <img src={allAgreed ? CheckElipse : Circle} alt="전체 동의" />
              <span className={allAgreed ? 'checked' : ''}>
                모든 항목에 동의 합니다.
              </span>
            </div>
          </div>
          <hr />
          <div className="agree-item">
            <div onClick={handlePersonalInfoToggle}>
              <img
                src={personalInfoAgreed ? ActiveCheck : Check}
                alt="개인정보 수집이용 동의"
              />
              <span>개인정보 수집이용 동의(필수)</span>
            </div>
            <span className="보기">보기</span>
          </div>
          <hr />
          <div className="agree-item">
            <div onClick={handleThirdPartyInfoToggle}>
              <img
                src={thirdPartyInfoAgreed ? ActiveCheck : Check}
                alt="제 3자 개인정보 이용 동의"
              />
              <span>제 3자 개인정보 이용 동의(필수)</span>
            </div>
            <span className="보기">보기</span>
          </div>

          <WideButton
            isActive={allAgreed}
            onClick={() => {
              if (allAgreed) {
                navigate('/signup/create');
              }
            }}
          >
            다음
          </WideButton>
        </div>
      </div>
    </>
  );
}

export default TermOfService;
