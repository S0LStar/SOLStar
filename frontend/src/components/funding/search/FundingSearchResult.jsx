import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import SearchReset from '../../../assets/common/SearchReset.png';
import GoBack from '../../../assets/common/GoBack.png';
import Go from '../../../assets/common/Go.png';
import temp from '../../../assets/character/Sol.png';
import Zzim from '../../../assets/artist/Zzim.png';
import NoZzim from '../../../assets/artist/NoZzim.png';

import './FundingSearchResult.css';
import FundingCard from '../common/FundingCard';
import axiosInstance from '../../../util/AxiosInstance';

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

    //  펀딩 검색 결과 데이터 API 연결
    const fetchFundingData = async () => {
      try {
        const response = await axiosInstance.get('/funding', {
          params: {
            keyword: query,
          },
        });

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

        console.log(response);
        setData((prevData) => ({
          ...prevData,
          fundingList: updatedFundingList,
        }));
      } catch (error) {
        console.error('검색 결과 funding 데이터', error);
      }
    };

    fetchFundingData();

    //  펀딩 검색 결과 데이터 API 연결
    const fetchArtistData = async () => {
      try {
        const response = await axiosInstance.get('/artist', {
          params: {
            keyword: query,
          },
        });

        const updatedArtistList = response.data.data.artistList.map(
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

        console.log(response);
        setData((prevData) => ({
          ...prevData,
          artistList: updatedArtistList,
        }));
      } catch (error) {
        console.error('검색 결과 funding 데이터', error);
      }
    };

    fetchFundingData();
    fetchArtistData();
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
            <FundingCard key={funding.fundingId} funding={funding} />
          ))}
        </div>
      </div>
    </div>
  );
}

export default FundingSearchResult;
