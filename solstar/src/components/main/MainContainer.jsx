import './MainContainer.css';

import SearchIcon from '../../assets/main/Search.png';
import Shoo from '../../assets/character/Shoo.png';
import Go from '../../assets/main/Go.png';

import ArtistFundingCard from './ArtistFundingCard';
import RecentPopularFundingCard from './RecentPopularFundingCard';

function MainContainer() {
  // TODO : 선호 아티스트 펀딩 임시 데이터
  const zzimArtistFundingTempData = [
    {
      fundingId: 1,
      imageSrc: '../../assets/character/Sol.png',
      artist: '장원영',
      title: '원영이 생일 기념 지하철 광고 부착',
      achievement: 347,
    },
    {
      fundingId: 2,
      imageSrc: '../../assets/character/Sol.png',
      artist: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      achievement: 110,
    },
    {
      fundingId: 3,
      imageSrc: '../../assets/character/Sol.png',
      artist: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !!',
      achievement: 80,
    },
  ];

  // TODO : 최근 인기 펀딩 임시 데이터
  const recentPopularFundingTempData = [
    {
      fundingId: 1,
      imageSrc: '../../assets/character/Sol.png',
      artist: '변우석',
      title: '선업튀 촬영장 조공',
      achievement: 347,
    },
    {
      fundingId: 2,
      imageSrc: '../../assets/character/Sol.png',
      artist: 'CIX',
      title: 'CIX 데뷔 5주년 기념',
      achievement: 110,
    },
    {
      fundingId: 3,
      imageSrc: '../../assets/character/Sol.png',
      artist: '공유',
      title: '우리 배우님 커피차 같이 쏠 사람 !!',
      achievement: 80,
    },
  ];

  // 선호 아티스트 펀딩 카드 핸들링 함수
  const handleArtistFundingCard = () => {};

  // 최근 인기 펀딩 카드 핸들링 함수
  const handleRecentPopularFundingCard = () => {};

  return (
    <>
      <div className="main-container">
        <div className="main-search-bar">
          <input
            type="text"
            className="main-search-input"
            placeholder="펀딩, 아티스트 검색"
          />
          <img src={SearchIcon} alt="search" className="main-search-icon" />
        </div>
        <div className="main-funding-regist-container">
          <div className="main-funding-regist-description">
            당신의 스타를 위한 SOL Star
          </div>
          <div className="main-funding-regist-under">
            <button className="main-funding-regist-button">펀딩 만들기</button>
            <img src={Shoo} alt="" className="main-sol-image" />
          </div>
        </div>
        <div className="main-zzim-artist-funding-list">
          <div className="zzim-artist-funding-header">
            <div className="zzim-artist-funding-description">
              나의 선호 아티스트 펀딩
            </div>
            <div className="main-view-all">
              전체보기
              <img src={Go} alt="" className="main-view-all-icon" />
            </div>
          </div>
          <div className="main-artist-funding-list">
            {zzimArtistFundingTempData.map((funding) => (
              <ArtistFundingCard
                key={funding.fundingId}
                funding={funding}
                onClick={handleArtistFundingCard}
              />
            ))}
          </div>
        </div>
        <div className="main-recent-popular-funding-list">
          <div className="recent-popular-funding-header">
            <div className="recent-popular-funding-description">
              최근 인기 펀딩
            </div>
            <div className="main-view-all">
              전체보기
              <img src={Go} alt="" className="main-view-all-icon" />
            </div>
          </div>
          <div className="main-popular-funding-list">
            {recentPopularFundingTempData.map((funding) => (
              <RecentPopularFundingCard
                key={funding.fundingId}
                funding={funding}
                onClick={handleRecentPopularFundingCard}
              />
            ))}
          </div>
        </div>
      </div>
    </>
  );
}

export default MainContainer;
