import './FundingRegist.css';
import BackButton from '../../common/BackButton';
import { useState } from 'react';
import FundingRegistInfo from './FundingRegistInfo';
import FundingRegistContent from './FundingRegistContent';
import axiosInstance from '../../../util/AxiosInstance';

function FundingRegistContainer() {
  const [step, setStep] = useState(1); // 펀딩 등록 단계
  const [funding, setFunding] = useState({
    // 펀딩 정보를 관리할 state
    type: 'COMMON',
    title: '',
    deadlineDate: '',
    goalAmount: 0,
    artistId: '',
    content: '',
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    if (id === 'goalAmount') {
      setFunding({ ...funding, goalAmount: parseInt(value) || 0 }); // 입력이 숫자가 아닐 경우 0으로 처리
    } else {
      setFunding({ ...funding, [id]: value });
    }
  };

  const handleRegist = () => {
    // 펀딩 생성 API 연결
    const registFunding = async () => {
      const response = await axiosInstance.post('funding', {
        type: funding.type,
        title: funding.title,
        deadlineDate: funding.deadlineDate,
        goalAmount: funding.goalAmount,
        artistId: funding.artistId,
        content: funding.content,
      });

      console.log(response);
    };

    registFunding();
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
