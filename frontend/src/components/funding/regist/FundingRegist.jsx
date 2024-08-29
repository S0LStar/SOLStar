import './FundingRegist.css';
import BackButton from '../../common/BackButton';
import { useState } from 'react';
import FundingRegistInfo from './FundingRegistInfo';
import FundingRegistContent from './FundingRegistContent';

function FundingRegistContainer() {
  const [step, setStep] = useState(1); // 펀딩 등록 단계
  const [funding, setFunding] = useState({
    // 펀딩 정보를 관리할 state
    type: 'COMMON',
    fundingImage: '',
    title: '',
    deadlineDate: '',
    goalAmount: 0,
    artistId: '',
    content: '',
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFunding({ ...funding, [id]: value });
  };

  const handleRegist = () => {
    // TODO: 등록 완료, API 연결 필요
    console.log(funding);
  };

  return (
    <div className="funding-regist-container">
      <header className="funding-regist-header">
        <BackButton />
        <div className="funding-regist-header-description">
          어떤 펀딩글 인가요 ?
        </div>
      </header>
      {step === 1 && (
        <FundingRegistInfo
          funding={funding}
          onChange={handleChange}
          onNext={() => {
            setStep(2);
          }}
        />
      )}
      {step === 2 && (
        <FundingRegistContent
          funding={funding}
          onChange={handleChange}
          onRegist={handleRegist}
        />
      )}
    </div>
  );
}

export default FundingRegistContainer;
