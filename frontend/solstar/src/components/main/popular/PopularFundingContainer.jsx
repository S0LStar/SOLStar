import { useNavigate } from 'react-router-dom';
import GoBack from '../../../assets/common/GoBack.png';
import './PopularFundingContainer.css';
import RecentPopularFundingCard from '../RecentPopularFundingCard';

// 최근 인기 펀딩 전체보기 페이지
function PopularFundingContainer() {
  const navigate = useNavigate();

  // TODO : 최근 인기 펀딩 임시 데이터
  const recentPopularFundingTempData = [
    {
      fundingId: 1,
      fundingImage: '../../assets/character/Sol.png',
      artistName: '변우석',
      title: '선업튀 촬영장 조공',
      successRate: 347,
      type: 'COMMON',
      status: 'PROCESSING',
      totalAmount: 28600000,
    },
    {
      fundingId: 2,
      fundingImage: '../../assets/character/Sol.png',
      artistName: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      successRate: 110,
      type: 'COMMON',
      status: 'SUCCESS',
      totalAmount: 18600000,
    },
    {
      fundingId: 3,
      fundingImage: '../../assets/character/Sol.png',
      artistName: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !!',
      successRate: 80,
      type: 'COMMON',
      status: 'FAIL',
      totalAmount: 1200000,
    },
    {
      fundingId: 4,
      fundingImage: '../../assets/character/Sol.png',
      artistName: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      successRate: 110,
      type: 'VERIFIED',
      status: 'PROCESSING',
      totalAmount: 1249800,
    },
    {
      fundingId: 5,
      fundingImage: '../../assets/character/Sol.png',
      artistName: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !! dafsdf',
      successRate: 80,
      type: 'COMMON',
      status: 'PROCESSING',
      totalAmount: 1970000,
    },
    {
      fundingId: 1,
      fundingImage: '../../assets/character/Sol.png',
      artistName: '변우석',
      title: '선업튀 촬영장 조공',
      successRate: 347,
      type: 'COMMON',
      status: 'PROCESSING',
      totalAmount: 28600000,
    },
    {
      fundingId: 2,
      fundingImage: '../../assets/character/Sol.png',
      artistName: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      successRate: 110,
      type: 'COMMON',
      status: 'SUCCESS',
      totalAmount: 18600000,
    },
    {
      fundingId: 3,
      fundingImage: '../../assets/character/Sol.png',
      artistName: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !!',
      successRate: 80,
      type: 'COMMON',
      status: 'FAIL',
      totalAmount: 1200000,
    },
    {
      fundingId: 4,
      fundingImage: '../../assets/character/Sol.png',
      artistName: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      successRate: 110,
      type: 'VERIFIED',
      status: 'PROCESSING',
      totalAmount: 1249800,
    },
    {
      fundingId: 5,
      fundingImage: '../../assets/character/Sol.png',
      artistName: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !! dafsdf',
      successRate: 80,
      type: 'COMMON',
      status: 'PROCESSING',
      totalAmount: 1970000,
    },
  ];

  return (
    <div className="popular-funding-container">
      <header className="popular-funding-header">
        <img src={GoBack} alt="" className="go-back" />
        <div className="popular-funding-header-description">최근 인기 펀딩</div>
      </header>
      <div className="popular-funding-list">
        {recentPopularFundingTempData.map((funding, index) => (
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
