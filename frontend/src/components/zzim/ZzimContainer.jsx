import './ZzimContainer.css';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import temp from '../../assets/character/Sol.png'; // Placeholder image
import Zzim from '../../assets/artist/Zzim.png';
import NoZzim from '../../assets/artist/NoZzim.png';
import axiosInstance from '../../util/AxiosInstance';

function ZzimContainer() {
  const navigate = useNavigate();
  const [zzimArtist, setZzimArtist] = useState([]);

  const artistData = async () => {
    try {
      const response = await axiosInstance.get('/artist/like');
      const updatedArtistList = response.data.data.likeArtistList.map(
        (artist) => ({
          ...artist,
          isLike: true,
        })
      );
      setZzimArtist(updatedArtistList);
      console.log(response.data);
    } catch (error) {
      console.log('test' + error);
    }
  };

  useEffect(() => {
    artistData();
    // [
    //   {
    //     artistId: 1,
    //     type: 'GROUP',
    //     name: '뉴진스',
    //     group: null,
    //     profileImage: 'artist_image_1_url',
    //     popularity: 230,
    //     isLike: true,
    //   },
    //   {
    //     artistId: 2,
    //     type: 'MEMBER',
    //     name: '민지',
    //     group: '뉴진스',
    //     profileImage: 'artist_image_2_url',
    //     popularity: 200,
    //     isLike: true,
    //   },
    //   {
    //     artistId: 3,
    //     type: 'MEMBER',
    //     name: '혜린',
    //     group: '뉴진스',
    //     profileImage: 'artist_image_3_url',
    //     popularity: 180,
    //     isLike: true,
    //   },
    //   {
    //     artistId: 4,
    //     type: 'MEMBER',
    //     name: '하니',
    //     group: '뉴진스',
    //     profileImage: 'artist_image_4_url',
    //     popularity: 160,
    //     isLike: true,
    //   },
    //   {
    //     artistId: 4,
    //     type: 'SOLO',
    //     name: '아이유',
    //     group: null,
    //     profileImage: 'artist_image_4_url',
    //     popularity: 160,
    //     isLike: true,
    //   },
    // ];
  }, []); // Add dependency array to run only once

  const toggleZzim = async (artistId) => {
    await axiosInstance.post(`/artist/like/${artistId}`);
    navigate(0);

    // setZzimArtist((prevArtists) =>
    //   prevArtists.map((artist) =>
    //     artist.artistId === artistId
    //       ? { ...artist, isLike: !artist.isLike }
    //       : artist
    //   )
    // );
  };

  return (
    <>
      <div className="zzim-container">
        <div className="zzim-header">찜 아티스트</div>
        <div className="artist-list">
          {zzimArtist.length === 0 ? (
            <div>
              <div className="no-artist-message">찜한 아티스트가 없습니다.</div>
            </div>
          ) : (
            zzimArtist.map((artist) => (
              <div key={artist.artistId} className="artist-item">
                <img
                  src={artist.profileImage || temp} // 기본 이미지 설정
                  alt={artist.name}
                  className="artist-image"
                  onClick={() => {
                    navigate(`/artist/${artist.artistId}`);
                  }}
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
                <div className="artist-text">
                  <span
                    className="artist-type"
                    onClick={() => {
                      navigate(`/artist/${artist.artistId}`);
                    }}
                  >
                    {artist.type === 'MEMBER'
                      ? artist.group
                      : artist.type === 'GROUP'
                        ? '그룹'
                        : 'SOLO'}
                  </span>
                  <span
                    className="artist-name"
                    onClick={() => {
                      navigate(`/artist/${artist.artistId}`);
                    }}
                  >
                    {artist.name}
                  </span>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </>
  );
}

export default ZzimContainer;
