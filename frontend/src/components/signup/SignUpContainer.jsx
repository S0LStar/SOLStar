import './SignUpContainer.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Shoo from '../../assets/character/Shoo.png';
import ProgressBar from './accountRegist/ProgressBar';
import LeftVector from '../../assets/common/LeftVector.png';

function SignUpContainer() {
  const navigate = useNavigate();
  const [currentStep, setCurrentStep] = useState(1);
  // 만약에 소속사 사용자 선택 있으면 0번으로 할 예정
  // const [currentStep, setCurrentStep] = useState(0);

  const handleNextStep = () => {
    setCurrentStep((prevStep) => Math.min(prevStep + 1, 5)); // 최대 4단계
  };

  const handlePrevStep = () => {
    setCurrentStep((prevStep) => Math.max(prevStep - 1, 0)); // 최소 1단계
  };

  return (
    <>
      <div className="signup-container">
        <div className="signup-header">
          <div className="signup-header-backInfo">
            {' '}
            <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />
            약관 동의
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <div>모든 항목에 동의 합니다.</div>
        <div>
          <span>개인정보 수집이용 동의(필수)</span>{' '}
          <span className="">보기</span>
        </div>
        <div>
          <span>제 3자 개인정보 이용 도의(필수)</span>{' '}
          <span className="">보기</span>
        </div>

        {/* <div>
          <div className="signup-content">
            {currentStep === 0 && <div>Step 0 Content</div>}
            {currentStep === 1 && <div>Step 1 Content</div>}
            {currentStep === 2 && <div>Step 2 Content</div>}
            {currentStep === 3 && <div>Step 3 Content</div>}
            {currentStep === 4 && <div>Step 4 Content</div>}
            {currentStep === 5 && <div>Step 5 Content</div>}
          </div>
          <div className="navigation-buttons">
            {currentStep > 0 && <button onClick={handlePrevStep}>이전</button>}
            {currentStep < 5 && <button onClick={handleNextStep}>다음</button>}
          </div>
        </div> */}
      </div>
    </>
  );
}

export default SignUpContainer;
