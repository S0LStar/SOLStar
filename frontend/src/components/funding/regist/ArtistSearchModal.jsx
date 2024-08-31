import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';

import './ArtistSearchModal.css';
import SearchIcon from '../../../assets/main/Search.png';
import DefaultArtistProfile from '../../../assets/common/DefaultArtist.png';
import axiosInstance from '../../../util/AxiosInstance';

function ArtistSearchModal({ isOpen, onClose, onSelectArtist }) {
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredArtists, setFilteredArtists] = useState([]);
  const [artists, setArtists] = useState([]);
  const [search, setSearch] = useState(false);

  useEffect(() => {
    const fetchArtistList = async () => {
      try {
        const response = await axiosInstance.get('/artist', {
          params: {
            keyword: '', // 초기 로드 시에는 키워드 없이 전체 아티스트 목록을 가져옵니다.
          },
        });

        setArtists(response.data.data.artistList);
        setFilteredArtists(response.data.data.artistList);
      } catch (error) {
        console.error(error);
      }
    };

    fetchArtistList();

    if (isOpen) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = 'auto';
    }

    return () => {
      document.body.style.overflow = 'auto';
    };
  }, [isOpen]);

  const handleSearch = () => {
    if (searchQuery.trim() === '') {
      // 검색어가 비어 있으면 전체 리스트를 다시 설정
      setFilteredArtists(artists);
    } else {
      const lowerCaseQuery = searchQuery.toLowerCase();
      const results = artists.filter((artist) =>
        artist.name.toLowerCase().includes(lowerCaseQuery)
      );
      setFilteredArtists(results);
    }
    setSearch(true);
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSearch();
    }
  };

  const handleOverlayClick = (e) => {
    if (e.target === e.currentTarget) {
      onClose();
    }
  };

  if (!isOpen) return null;

  return (
    <div className="artist-search-modal-overlay" onClick={handleOverlayClick}>
      <div className="artist-search-modal-content">
        <header className="artist-search-modal-header">
          <input
            type="text"
            placeholder="아티스트 이름을 입력하세요"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onKeyDown={handleKeyPress}
          />
          <img
            src={SearchIcon}
            alt="search"
            className="main-search-icon"
            onClick={handleSearch}
          />
        </header>
        <ul className="artist-search-modal-list">
          {searchQuery === '' &&
            !search &&
            artists.map((artist) => (
              <li
                key={artist.artistId}
                onClick={() => {
                  onSelectArtist(artist);
                  onClose(); // 선택 후 모달 닫기
                }}
              >
                <img
                  src={artist.profileImage || DefaultArtistProfile}
                  alt=""
                  className="search-modal-default-artist-profile"
                />
                <div>{artist.name}</div>
              </li>
            ))}
          {search && filteredArtists.length > 0 ? (
            filteredArtists.map((artist) => (
              <li
                key={artist.artistId}
                onClick={() => {
                  onSelectArtist(artist);
                  onClose(); // 선택 후 모달 닫기
                }}
              >
                <img
                  src={artist.profileImage || DefaultArtistProfile}
                  alt=""
                  className="search-modal-default-artist-profile"
                />
                <div>{artist.name}</div>
              </li>
            ))
          ) : search && filteredArtists.length === 0 ? (
            <div className="no-results">일치하는 아티스트가 없습니다.</div>
          ) : null}
        </ul>
      </div>
    </div>
  );
}

ArtistSearchModal.propTypes = {
  isOpen: PropTypes.bool.isRequired,
  onClose: PropTypes.func.isRequired,
  onSelectArtist: PropTypes.func.isRequired,
};

export default ArtistSearchModal;
