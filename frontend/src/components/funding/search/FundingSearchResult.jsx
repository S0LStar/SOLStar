import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import SearchReset from '../../../assets/common/SearchReset.png';
import GoBack from '../../../assets/common/GoBack.png';
import Go from '../../../assets/common/Go.png';
import Zzim from '../../../assets/artist/Zzim.png';
import NoZzim from '../../../assets/artist/NoZzim.png';
import DefaultArtist from '../../../assets/common/DefaultArtist.png';

import './FundingSearchResult.css';
import FundingCard from '../common/FundingCard';
import axiosInstance from '../../../util/AxiosInstance';
import Empty from '../../common/Empty';

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

  const toggleZzim = (artistId, event) => {
    event.stopPropagation();

    // 찜/찜 해제 API 요청
    const postLike = async () => {
      const response = await axiosInstance.post(`/artist/like/${artistId}`);

      console.log(response);
    };

    postLike();

    setData((prevData) => {
      const updatedArtistList = prevData.artistList.map((artist) => {
        if (artist.artistId === artistId) {
          return { ...artist, liked: !artist.liked };
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
          {data.artistList.length > 0 ? (
            data.artistList.map((artist) => (
              <div
                key={artist.artistId}
                className="artist-item"
                onClick={() => {
                  navigate(`/artist/${artist.artistId}`);
                }}
              >
                <img
                  src={artist.profileImage || DefaultArtist}
                  alt={artist.name}
                  className="artist-image"
                />
                {artist.liked ? (
                  <img
                    src={Zzim}
                    alt=""
                    className="artist-item-zzim"
                    onClick={(e) => toggleZzim(artist.artistId, e)}
                  />
                ) : (
                  <img
                    src={NoZzim}
                    alt=""
                    className="artist-item-zzim"
                    onClick={(e) => toggleZzim(artist.artistId, e)}
                  />
                )}
                <span className="artist-name">{artist.name}</span>
              </div>
            ))
          ) : (
            <Empty>해당하는 아티스트</Empty>
          )}
        </div>
      </div>

      <div className="search-results-section">
        <div className="search-result-funding">
          <h2>펀딩</h2>
          <img src={Go} alt="" className="search-result-go" />
        </div>
        <div className="search-result-funding-list">
          {data.fundingList.length > 0 ? (
            data.fundingList.map((funding) => (
              <FundingCard key={funding.fundingId} funding={funding} />
            ))
          ) : (
            <Empty>해당하는 펀딩</Empty>
          )}
        </div>
      </div>
    </div>
  );
}

export default FundingSearchResult;
