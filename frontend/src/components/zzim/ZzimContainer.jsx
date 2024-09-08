import './ZzimContainer.css';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import temp from '../../assets/character/Sol.png'; // Placeholder image
import Zzim from '../../assets/artist/Zzim.png';
import NoZzim from '../../assets/artist/NoZzim.png';
import axiosInstance from '../../util/AxiosInstance';
import Empty from '../common/Empty';

function ZzimContainer() {
  const navigate = useNavigate();
  const [zzimArtist, setZzimArtist] = useState([]);

  useEffect(() => {
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
    artistData();
  }, []);

  const toggleZzim = async (artistId) => {
    try {
      await axiosInstance.post(`/artist/like/${artistId}`);
      setZzimArtist((prevArtists) =>
        prevArtists.map((artist) =>
          artist.artistId === artistId
            ? { ...artist, isLike: !artist.isLike } // 찜 상태 반전
            : artist
        )
      );
    } catch (error) {
      console.error('찜 상태 변경 중 오류 발생:', error);
    }
  };

  return (
    <>
      <div className="zzim-container">
        <div className="zzim-header">찜 아티스트</div>
        <div className="artist-list">
          {zzimArtist.length === 0 ? (
            <div>
              <Empty>찜한 아티스트</Empty>
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
