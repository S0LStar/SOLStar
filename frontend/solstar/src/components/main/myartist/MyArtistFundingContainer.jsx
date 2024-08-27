import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BackButton from '../../common/BackButton';
import FundingCard from '../../funding/common/FundingCard';
import './MyArtistFundingContainer.css';

function MyArtistFundingContainer() {
  const navigate = useNavigate();
  const [fundings, setFundings] = useState([]);

  useEffect(() => {
    // TODO : 나의 선호 아티스트 펀딩 리스트 api 연결
    // 선호 아티스트 펀딩 리스트 Data
    const fetchFunding = [
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
    ];

    setFundings(fetchFunding);
  }, []);

  return (
    <div className="my-artist-funding-container">
      <header className="my-artist-funding-header">
        <BackButton />
        <div className="my-artist-funding-header-description">
          나의 선호 아티스트 펀딩
        </div>
      </header>
      <div className="my-artist-funding-list">
        {fundings.map((funding, index) => (
          <FundingCard
            key={funding.fundingId}
            index={index}
            funding={funding}
            onClick={() => {
              navigate(`/funding/${funding.fundingId}`);
            }}
          />
        ))}
      </div>
    </div>
  );
}

export default MyArtistFundingContainer;
