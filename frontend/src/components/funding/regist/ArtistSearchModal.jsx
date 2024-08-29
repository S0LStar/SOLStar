import PropTypes from 'prop-types';
import { useEffect, useState } from 'react';

import './ArtistSearchModal.css';
import SearchIcon from '../../../assets/main/Search.png';
import DefaultArtistProfile from '../../../assets/common/DefaultArtist.png';

function ArtistSearchModal({ isOpen, onClose, onSelectArtist }) {
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredArtists, setFilteredArtists] = useState('');

  // 모달이 열릴 때, 외부 화면 스크롤 비활성화
  useEffect(() => {
    if (isOpen) {
      // 모달이 열리면 body의 스크롤을 비활성화
      document.body.style.overflow = 'hidden';
    } else {
      // 모달이 닫히면 body의 스크롤을 다시 활성화
      document.body.style.overflow = 'auto';
    }

    // 모달이 unmount 시에 스크롤 초기화
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, [isOpen]);

  // TODO: tempData
  const [artists] = useState([
    { artistId: 1, name: '아티스트 1', profileImage: null },
    { artistId: 2, name: '아티스트 2', profileImage: null },
    { artistId: 3, name: '아티스트 3', profileImage: null },
    { artistId: 4, name: '아티스트 1', profileImage: null },
    { artistId: 5, name: '아티스트 2', profileImage: null },
    { artistId: 13, name: '아티스트 3', profileImage: null },
    { artistId: 11, name: '아티스트 1', profileImage: null },
    { artistId: 12, name: '아티스트 2', profileImage: null },
    { artistId: 6, name: '아티스트 3', profileImage: null },
    { artistId: 9, name: '아티스트 1', profileImage: null },
    { artistId: 22, name: '아티스트 2', profileImage: null },
    { artistId: 31, name: '아티스트 3', profileImage: null },
  ]);

  // 검색 결과와 일치하는 아티스트 반환
  const handleSearch = () => {
    const results = artists.filter((artist) =>
      artist.name.includes(searchQuery)
    );
    setFilteredArtists(results);
  };

  // 엔터키 눌렀을 때 검색 결과 반환 함수 실행
  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      handleSearch();
    }
  };

  // 모달 바깥 영역 클릭 시 모달 닫힘
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
          {filteredArtists &&
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
            ))}
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
