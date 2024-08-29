import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BackButton from '../../common/BackButton';
import FundingCard from '../../funding/common/FundingCard';
import './MyArtistFundingContainer.css';
import axiosInstance from '../../../util/AxiosInstance';

function MyArtistFundingContainer() {
  const navigate = useNavigate();
  const [artistFunding, setArtistFunding] = useState([]); // 나의 선호 아티스트 펀딩 상태 관리

  useEffect(() => {
    // 나의 선호 아티스트 펀딩 리스트 api 연결
    // 선호 아티스트 펀딩 리스트 Data
    const fetchArtistFunding = async () => {
      // 나의 선호 아티스트 펀딩 조회 API 요청
      try {
        const response = await axiosInstance.get('/funding/my-like-artist');
        console.log(response);

        const updatedFundingList = response.data.data.fundingList.map(
          (funding) => {
            // successRate 계산: totalAmount / goalAmount * 100
            const successRate = Math.floor(
              (funding.totalAmount / funding.goalAmount) * 100
            );

            // successRate를 포함한 새로운 객체 반환
            return {
              ...funding,
              successRate: successRate,
            };
          }
        );

        setArtistFunding(updatedFundingList);
      } catch (error) {
        console.error(error);
      }
    };

    fetchArtistFunding();
  }, []);

  return (
    <div className="my-artist-funding-container">
      <header className="my-artist-funding-header">
        <BackButton />
        <div className="my-artist-funding-header-description">
          나의 선호 아티스트 펀딩
        </div>
      </header>
      <div className="my-artist-funding-list">
        {artistFunding.map((funding, index) => (
          <FundingCard
            key={funding.fundingId}
            index={index}
            funding={funding}
            onClick={() => {
              navigate(`/funding/${funding.fundingId}`);
            }}
          />
        ))}
      </div>
    </div>
  );
}

export default MyArtistFundingContainer;
