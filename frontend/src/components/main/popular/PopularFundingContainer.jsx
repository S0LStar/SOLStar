import { useNavigate } from 'react-router-dom';
import './PopularFundingContainer.css';
import BackButton from '../../common/BackButton';
import RecentPopularFundingCard from '../RecentPopularFundingCard';
import { useEffect, useState } from 'react';
import axiosInstance from '../../../util/AxiosInstance';

// 최근 인기 펀딩 전체보기 페이지
function PopularFundingContainer() {
  const navigate = useNavigate();
  const [recentPopularFunding, setRecentPopularFunding] = useState([]);

  useEffect(() => {
    // 최근 인기 펀딩
    const fetchRecentPopularFunding = async () => {
      // 최근 인기 펀딩 조회 API 연결
      const response = await axiosInstance.get('/funding/popular');
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

      setRecentPopularFunding(updatedFundingList);
    };

    fetchRecentPopularFunding();
  }, []);

  return (
    <div className="popular-funding-container">
      <header className="popular-funding-header">
        <BackButton />
        <div className="popular-funding-header-description">최근 인기 펀딩</div>
      </header>
      <div className="popular-funding-list">
        {recentPopularFunding.slice(0, 10).map((funding, index) => (
          <RecentPopularFundingCard
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

export default PopularFundingContainer;
