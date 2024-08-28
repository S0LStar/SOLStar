import './MyArtist.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import temp from '../../assets/character/Sol.png'; // Placeholder image
import Zzim from '../../assets/artist/Zzim.png';
import NoZzim from '../../assets/artist/NoZzim.png';
import WideButton from '../common/WideButton';
import { Navigate } from 'react-router-dom';

function MyArtist() {
  const [zzimArtist, setZzimArtist] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const artistData = [
      {
        artistId: 1,
        type: 'GROUP',
        name: '뉴진스',
        group: null,
        profileImage: 'artist_image_1_url',
        popularity: 230,
        isLike: true,
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
        isLike: true,
      },
      {
        artistId: 4,
        type: 'MEMBER',
        name: '하니',
        group: '뉴진스',
        profileImage: 'artist_image_4_url',
        popularity: 160,
        isLike: true,
      },
      {
        artistId: 4,
        type: 'SOLO',
        name: '아이유',
        group: null,
        profileImage: 'artist_image_4_url',
        popularity: 160,
        isLike: true,
      },
    ];
    setZzimArtist(artistData);
  }, []); // Add dependency array to run only once

  const toggleZzim = (artistId) => {
    setZzimArtist((prevArtists) =>
      prevArtists.map((artist) =>
        artist.artistId === artistId
          ? { ...artist, isLike: !artist.isLike }
          : artist
      )
    );
  };

  return (
    <>
      <div className="myartist-container">
        <div className="myartist-header">소속 아티스트</div>
        <div className="artist-list">
          {zzimArtist.map((artist) => (
            <div key={artist.artistId} className="artist-item">
              <img src={temp} alt={artist.name} className="artist-image" />
              {artist.isLike ? (
                <img
                  src={Zzim}
                  alt="liked"
                  className="artist-item-zzim"
                  onClick={() => toggleZzim(artist.artistId)}
                />
              ) : (
                <img
                  src={NoZzim}
                  alt="not liked"
                  className="artist-item-zzim"
                  onClick={() => toggleZzim(artist.artistId)}
                />
              )}
              <div className="artist-text">
                <span className="artist-type">
                  {artist.type === 'MEMBER'
                    ? artist.group
                    : artist.type === 'GROUP'
                      ? '그룹'
                      : 'SOLO'}
                </span>
                <span className="artist-name">{artist.name}</span>
              </div>
            </div>
          ))}
        </div>
        <WideButton
          // onClick={() => navigate('/agencymy/createartist')}
          onClick={() => {}}
          isActive={true}
        >
          추가
        </WideButton>
      </div>
    </>
  );
}

export default MyArtist;
