import './ZzimContainer.css';
import { useEffect, useState } from 'react';
import temp from '../../assets/character/Sol.png'; // Placeholder image
import Zzim from '../../assets/artist/Zzim.png';
import NoZzim from '../../assets/artist/NoZzim.png';

function ZzimContainer() {
  const [zzimArtist, setZzimArtist] = useState([]);

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
      <div className="zzim-container">
        <div className="zzim-header">찜 아티스트</div>
        <div className="artist-list">
          {zzimArtist.map((artist) => (
            <div key={artist.artistId} className="artist-item">
              <img
                src={artist.profileImage || temp}
                alt={artist.name}
                className="artist-image"
              />
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
              <span className="artist-name">{artist.name}</span>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default ZzimContainer;
