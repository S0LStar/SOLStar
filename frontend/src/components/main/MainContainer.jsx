import './MainContainer.css';

import SearchIcon from '../../assets/main/Search.png';
import Shoo from '../../assets/character/Shoo.png';
import Go from '../../assets/main/Go.png';

import ArtistFundingCard from './ArtistFundingCard';
import RecentPopularFundingCard from './RecentPopularFundingCard';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../util/AxiosInstance';
import { useEffect, useState } from 'react';

function MainContainer() {
  const navigate = useNavigate();
  const [zzimArtistFundingTempData, setZzimArtistFundingTempData] = useState(
    []
  );

  useEffect(() => {
    // 나의 선호 아티스트 펀딩 조회 API 요청
    const fetchArtistFunding = async () => {
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

        setZzimArtistFundingTempData(updatedFundingList);
      } catch (error) {
        console.error(error);
      }
    };

    fetchArtistFunding();
  }, []);

  // TODO : 최근 인기 펀딩 임시 데이터
  // status : 0: 펀딩 진행 중, 1: 펀딩 성공, -1: 펀딩 실패
  // type : 펀딩 타입 (0:일반 펀딩, 1:인증 펀딩)
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
      fundingId: 6,
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
    <>
      <div className="main-container">
        <div className="main-search-bar">
          <input
            type="text"
            className="main-search-input"
            placeholder="펀딩, 아티스트 검색"
            onClick={() => {
              navigate('/search');
            }}
            readOnly
          />
          <img src={SearchIcon} alt="search" className="main-search-icon" />
        </div>
        <div className="main-funding-regist-container">
          <div className="main-funding-regist-description">
            당신의 스타를 위한 SOL Star
          </div>
          <div className="main-funding-regist-under">
            <button
              className="main-funding-regist-button"
              onClick={() => {
                navigate('/funding/regist');
              }}
            >
              펀딩 만들기
            </button>
            <img src={Shoo} alt="" className="main-sol-image" />
          </div>
        </div>
        <div className="main-zzim-artist-funding-list">
          <div className="zzim-artist-funding-header">
            <div className="zzim-artist-funding-description">
              나의 선호 아티스트 펀딩
            </div>
            <div
              className="main-view-all"
              onClick={() => {
                navigate('/my-artist');
              }}
            >
              전체보기
              <img src={Go} alt="" className="main-view-all-icon" />
            </div>
          </div>
          <div className="main-artist-funding-list">
            {zzimArtistFundingTempData.slice(0, 10).map((funding) => (
              <ArtistFundingCard
                key={funding.fundingId}
                funding={funding}
                onClick={() => {
                  navigate(`/funding/${funding.fundingId}`);
                }}
              />
            ))}
          </div>
        </div>
        <div className="main-recent-popular-funding-list">
          <div className="recent-popular-funding-header">
            <div className="recent-popular-funding-description">
              최근 인기 펀딩
            </div>
            <div
              className="main-view-all"
              onClick={() => {
                navigate('/popular');
              }}
            >
              전체보기
              <img src={Go} alt="" className="main-view-all-icon" />
            </div>
          </div>
          <div className="main-popular-funding-list">
            {recentPopularFundingTempData.slice(0, 5).map((funding, index) => (
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
      </div>
    </>
  );
}

export default MainContainer;
