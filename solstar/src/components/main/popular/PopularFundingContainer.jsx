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
      imageSrc: '../../assets/character/Sol.png',
      artist: '변우석',
      title: '선업튀 촬영장 조공',
      achievement: 347,
      isCertification: true,
      status: 0,
    },
    {
      fundingId: 2,
      imageSrc: '../../assets/character/Sol.png',
      artist: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      achievement: 110,
      isCertification: false,
      status: 1,
    },
    {
      fundingId: 3,
      imageSrc: '../../assets/character/Sol.png',
      artist: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !!',
      achievement: 80,
      isCertification: true,
      status: 0,
    },
    {
      fundingId: 4,
      imageSrc: '../../assets/character/Sol.png',
      artist: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      achievement: 110,
      isCertification: false,
      status: 2,
    },
    {
      fundingId: 5,
      imageSrc: '../../assets/character/Sol.png',
      artist: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !! dafsdf',
      achievement: 80,
      isCertification: true,
      status: 0,
    },
    {
      fundingId: 6,
      imageSrc: '../../assets/character/Sol.png',
      artist: '변우석',
      title: '선업튀 촬영장 조공',
      achievement: 347,
      isCertification: true,
      status: 0,
    },
    {
      fundingId: 8,
      imageSrc: '../../assets/character/Sol.png',
      artist: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      achievement: 110,
      isCertification: false,
      status: 1,
    },
    {
      fundingId: 7,
      imageSrc: '../../assets/character/Sol.png',
      artist: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !!',
      achievement: 80,
      isCertification: true,
      status: 0,
    },
    {
      fundingId: 9,
      imageSrc: '../../assets/character/Sol.png',
      artist: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      achievement: 110,
      isCertification: false,
      status: 2,
    },
    {
      fundingId: 12,
      imageSrc: '../../assets/character/Sol.png',
      artist: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !! dafsdf',
      achievement: 80,
      isCertification: true,
      status: 0,
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
