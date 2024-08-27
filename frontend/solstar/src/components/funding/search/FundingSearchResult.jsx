import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import SearchReset from '../../../assets/common/SearchReset.png';
import GoBack from '../../../assets/common/GoBack.png';
import Go from '../../../assets/common/Go.png';
import temp from '../../../assets/character/Sol.png';
import Zzim from '../../../assets/artist/Zzim.png';
import NoZzim from '../../../assets/artist/NoZzim.png';

import './FundingSearchResult.css';

function FundingSearchResult() {
  const location = useLocation();
  const navigate = useNavigate();
  const query = new URLSearchParams(location.search).get('query');
  const [searchValue, setSearchValue] = useState(query || '');

  // api를 통한 검색 결과 data
  const [data, setData] = useState({
    fundingList: [],
    artistList: [],
  });

  useEffect(() => {
    console.log(query);
    // TODO : API 연결
    // 검색 결과 데이터
    const fetchedData = {
      fundingList: [
        {
          fundingId: 1,
          type: 'VERIFIED',
          artistName: '뉴진스',
          title: '뉴진스 데뷔 2주년 기념🎉 2호선을 뉴진스로 물들여요!',
          fundingImage: 'image',
          successRate: 372,
          totalAmount: 18600000,
          status: 'PROCESSING',
          remainDays: 22,
        },
        {
          fundingId: 2,
          type: 'COMMON',
          artistName: '민지 (NewJeans)',
          title:
            '뉴진스 민지의 이름으로 따뜻한 마음을 전해요 💙 펀딩이 함께하는 사랑의 기부',
          fundingImage: '../../../assets/character/Sol.png',
          successRate: 160,
          totalAmount: 1600000,
          status: 'SUCCESS',
          remainDays: null,
        },
        {
          fundingId: 3,
          type: 'COMMON',
          artistName: '뉴진스',
          title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
          fundingImage: 'funding_image_3_url',
          successRate: 50,
          totalAmount: 1230000,
          status: 'FAIL',
          remainDays: null,
        },
        {
          fundingId: 4,
          type: 'COMMON',
          artistName: '뉴진스',
          title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
          fundingImage: 'funding_image_3_url',
          successRate: 50,
          totalAmount: 1230000,
          status: 'FAIL',
          remainDays: null,
        },
      ],
      artistList: [
        {
          artistId: 1,
          type: 'GROUP',
          name: '뉴진스',
          group: null,
          profileImage: 'artist_image_1_url',
          popularity: 230,
          isLike: false,
        },
        {
          artistId: 2,
          type: 'MEMBER',
          name: '민지',
          group: '뉴진스',
          profileImage: 'artist_image_2_url',
          popularity: 200,
          isLike: true,
        },
        {
          artistId: 3,
          type: 'MEMBER',
          name: '혜린',
          group: '뉴진스',
          profileImage: 'artist_image_3_url',
          popularity: 180,
          isLike: false,
        },
        {
          artistId: 4,
          type: 'MEMBER',
          name: '하니',
          group: '뉴진스',
          profileImage: 'artist_image_4_url',
          popularity: 160,
          isLike: false,
        },
      ],
    };

    setData(fetchedData); // 받아온 데이터를 상태로 설정
  }, [query]);

  const handleChange = (e) => {
    setSearchValue(e.target.value);
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      navigate(`/funding/search?query=${searchValue}`, { replace: true });
    }
  };

  const toggleZzim = (artistId) => {
    // TODO: 찜/찜 해제 API 요청
    setData((prevData) => {
      const updatedArtistList = prevData.artistList.map((artist) => {
        if (artist.artistId === artistId) {
          return { ...artist, isLike: !artist.isLike };
        }
        return artist;
      });

      return { ...prevData, artistList: updatedArtistList };
    });
  };

  return (
    <div className="search-results-container">
      <div className="main-search-bar">
        <img
          src={GoBack}
          alt=""
          className="go-back"
          onClick={() => {
            navigate('/');
          }}
        />
        <div className="main-search-result-input-container">
          <input
            type="text"
            className="main-search-result-input"
            placeholder="펀딩, 아티스트 검색"
            value={searchValue}
            onChange={handleChange}
            onKeyDown={handleKeyDown}
          />
          <img
            src={SearchReset}
            alt=""
            onClick={() => {
              setSearchValue('');
            }}
          />
        </div>
      </div>

      <div className="search-results-section">
        <div className="search-result-artist">
          <h2>아티스트</h2>
          <img src={Go} alt="" className="search-result-go" />
        </div>
        <div className="artist-list">
          {data.artistList.map((artist) => (
            <div key={artist.artistId} className="artist-item">
              <img src={temp} alt={artist.name} className="artist-image" />
              {artist.isLike ? (
                <img
                  src={Zzim}
                  alt=""
                  className="artist-item-zzim"
                  onClick={() => toggleZzim(artist.artistId)}
                />
              ) : (
                <img
                  src={NoZzim}
                  alt=""
                  className="artist-item-zzim"
                  onClick={() => toggleZzim(artist.artistId)}
                />
              )}
              <span className="artist-name">{artist.name}</span>
            </div>
          ))}
        </div>
      </div>

      <div className="search-results-section">
        <div className="search-result-funding">
          <h2>펀딩</h2>
          <img src={Go} alt="" className="search-result-go" />
        </div>
        <div className="search-result-funding-list">
          {data.fundingList.map((funding) => (
            <div key={funding.fundingId} className="search-result-funding-item">
              <img src={temp} alt="" className="search-result-funding-image" />
              <div className="search-result-funding-info">
                <span className="search-result-funding-artist">
                  {funding.artistName}
                </span>
                <h3 className="search-result-funding-title">{funding.title}</h3>
                <div
                  className={`search-result-funding-details 
                    ${
                      funding.status === 'PROCESSING'
                        ? 'progress'
                        : funding.status === 'SUCCESS' ||
                            funding.status === 'CLOSED'
                          ? 'success'
                          : 'fail'
                    }`}
                  style={{ '--achievement-percentage': funding.successRate }}
                >
                  <div className="search-result-funding-detail">
                    <span className="search-result-funding-progress">
                      {funding.successRate}%
                    </span>
                    <span className="search-result-funding-amount">
                      {funding.totalAmount.toLocaleString()}원
                    </span>
                    {funding.remainDays ? (
                      <span className="search-result-funding-days-left">
                        {funding.remainDays}일 남음
                      </span>
                    ) : funding.status === 'SUCCESS' ? (
                      <span className="search-result-funding-days-left">
                        펀딩 성공
                      </span>
                    ) : (
                      <span className="search-result-funding-days-left">
                        펀딩 무산
                      </span>
                    )}
                  </div>
                  <div className="popular-funding-achievement-bar"></div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default FundingSearchResult;
