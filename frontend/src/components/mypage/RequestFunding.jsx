import './RequestFunding.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AxiosInstance from '../../util/AxiosInstance'; // AxiosInstance import
import WideButton from '../common/WideButton';
import FundingCard from '../funding/common/FundingCard';

function RequestFunding() {
  const navigate = useNavigate();
  const [fundingData, setFundingData] = useState([]); // 상태 추가
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  useEffect(() => {
    const fetchFundingData = async () => {
      try {
        const response = await AxiosInstance.get('/agency/funding'); // `/agency/funding` 엔드포인트로 GET 요청
        setFundingData(response.data.data); // 응답 데이터를 상태로 설정
      } catch (error) {
        console.error('펀딩 데이터를 가져오는 데 실패했습니다:', error);
        setError('펀딩 데이터를 가져오는 데 실패했습니다.');
      } finally {
        setLoading(false); // 로딩 상태 업데이트
      }
    };

    fetchFundingData(); // 데이터 가져오기
  }, []);

  const handleCancel = async (fundingId) => {
    try {
      const response = await AxiosInstance.patch(
        `/agency/funding-reject/${fundingId}`
      );
      console.log(`Funding ${fundingId} cancelled:`, response.data);
      // 상태 업데이트를 통해 UI를 새로고침할 수 있습니다.
      setFundingData((prevData) =>
        prevData.filter((funding) => funding.fundingId !== fundingId)
      );
    } catch (error) {
      console.error(`Funding ${fundingId} cancellation failed:`, error);
      alert('펀딩 거절에 실패했습니다.');
    }
  };

  const handleComplete = async (fundingId) => {
    try {
      const response = await AxiosInstance.patch(
        `/agency/funding-accept/${fundingId}`
      );
      console.log(`Funding ${fundingId} completed:`, response.data);
      // 상태 업데이트를 통해 UI를 새로고침할 수 있습니다.
      setFundingData((prevData) =>
        prevData.filter((funding) => funding.fundingId !== fundingId)
      );
    } catch (error) {
      console.error(`Funding ${fundingId} acceptance failed:`, error);
      alert('펀딩 승인에 실패했습니다.');
    }
  };

  if (loading) {
    return <div>Loading...</div>; // 로딩 중일 때 표시
  }

  if (error) {
    return <div>{error}</div>; // 에러가 발생했을 때 표시
  }

  return (
    <>
      <div className="request-container">
        <div className="request-funding">인증 펀딩 요청</div>
        <div className="request-funding-list">
          {fundingData.map((funding) => (
            <div className="request-funding-item" key={funding.fundingId}>
              <FundingCard
                funding={{
                  ...funding,
                  totalAmount: funding.totalAmount || 0, // 기본값 설정
                  successRate: funding.successRate || 0, // 기본값 설정
                  goalAmount: funding.goalAmount || 1, // 기본값 설정으로 나누기 오류 방지
                }}
              />
              <div className="request-funding-buttons">
                <WideButton
                  onClick={() => handleCancel(funding.fundingId)}
                  isActive={true}
                >
                  거절
                </WideButton>
                <WideButton
                  onClick={() => handleComplete(funding.fundingId)}
                  isActive={true}
                >
                  승인
                </WideButton>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default RequestFunding;
