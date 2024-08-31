import { useEffect, useState } from 'react';
import BackButton from '../common/BackButton';
import axiosInstance from '../../util/AxiosInstance';
import { useParams } from 'react-router-dom';
import DefaultArtist from '../../assets/common/DefaultArtist.png';
import FundingCard from '../funding/common/FundingCard';
import Loading from '../common/Loading';
import './ArtistFunding.css';
import Empty from '../common/Empty';

function MyArtistFunding() {
  const { artistId } = useParams();
  const [artist, setArtist] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 특정 아티스트 펀딩 리스트 조회
    const fetchArtistData = async () => {
      try {
        const response = await axiosInstance.get(`/artist/${artistId}`);
        console.log(response);

        setArtist(response.data.data);
      } catch (error) {
        console.error('error', error);
      } finally {
        setLoading(false);
      }
    };

    fetchArtistData();
  }, []);

  if (loading) {
    return (
      <Loading>
        <span style={{ color: '#0046ff' }}>아티스트 펀딩 내역</span>을
        <br /> 가져오는 중이에요
      </Loading>
    );
  }

  return (
    <div className="artist-funding-detail">
      <div className="artist-funding-header">
        <BackButton />
        {/* TODO: artist.profileImage ||  */}
        <img src={DefaultArtist} alt="" />
        <div className="artist-funding-info">
          {artist.type === 'GROUP' ? (
            <div>
              그룹{' '}
              <span className="artist-funding-artist-name">{artist.name}</span>
            </div>
          ) : (
            <div>
              아티스트{' '}
              <span className="artist-funding-artist-name">{artist.name}</span>
            </div>
          )}
        </div>
      </div>
      <div className="artist-funding-list">
        {artist.fundingList && artist.fundingList.length > 0 ? (
          artist.fundingList.map((funding) => (
            <FundingCard key={funding.fundingId} funding={funding} />
          ))
        ) : (
          <Empty>등록된 펀딩</Empty>
        )}
      </div>
    </div>
  );
}

export default MyArtistFunding;
