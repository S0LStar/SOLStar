import './MainContainer.css';

import SearchIcon from '../../assets/main/Search.png';
import Shoo from '../../assets/character/Shoo.png';
import Go from '../../assets/main/Go.png';

import ArtistFundingCard from './ArtistFundingCard';
import RecentPopularFundingCard from './RecentPopularFundingCard';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../util/AxiosInstance';
import { useEffect, useState } from 'react';
import Empty from '../common/Empty';

function MainContainer() {
  const navigate = useNavigate();
  const [zzimArtistFunding, setZzimArtistFunding] = useState([]); // 나의 선호 아티스트 펀딩 상태 관리
  const [recentPopularFunding, setRecentPopularFunding] = useState([]); // 최근 인기 펀딩 상태 관리

  useEffect(() => {
    // 나의 선호 아티스트 펀딩
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

        setZzimArtistFunding(updatedFundingList);
      } catch (error) {
        console.error(error);
      }
    };

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

    fetchArtistFunding();
    fetchRecentPopularFunding();
  }, []);

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
            {zzimArtistFunding.length > 0 ? (
              zzimArtistFunding.slice(0, 10).map((funding) => (
                <ArtistFundingCard
                  key={funding.fundingId}
                  funding={funding}
                  onClick={() => {
                    navigate(`/funding/${funding.fundingId}`);
                  }}
                />
              ))
            ) : (
              <Empty>선호 아티스트의 펀딩</Empty>
            )}
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
            {recentPopularFunding.length > 0 ? (
              recentPopularFunding.slice(0, 5).map((funding, index) => (
                <RecentPopularFundingCard
                  key={funding.fundingId}
                  index={index}
                  funding={funding}
                  onClick={() => {
                    navigate(`/funding/${funding.fundingId}`);
                  }}
                />
              ))
            ) : (
              <Empty>최근 인기 펀딩</Empty>
            )}
          </div>
        </div>
      </div>
    </>
  );
}

export default MainContainer;
