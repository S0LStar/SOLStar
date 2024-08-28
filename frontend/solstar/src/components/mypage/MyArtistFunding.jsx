import './RequestFunding.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import WideButton from '../common/WideButton';
import temp from '../../assets/character/Shoo.png';
import LeftVector from '../../assets/common/LeftVector.png';

import FundingCard from '../funding/common/FundingCard';

function MyArtistFunding() {
  const navigate = useNavigate();

  const fundingData = [
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
    {
      fundingId: 3,
      type: 'COMMON',
      artistName: '뉴진스',
      title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
      fundingImage: 'funding_image_3_url',
      successRate: 50,
      totalAmount: 1230000,
      status: 'FAIL',
      remainDays: null,
    },
    {
      fundingId: 4,
      type: 'COMMON',
      artistName: '뉴진스',
      title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
      fundingImage: 'funding_image_3_url',
      successRate: 50,
      totalAmount: 1230000,
      status: 'FAIL',
      remainDays: null,
    },
  ];

  const handleCancel = (fundingId) => {
    // API 연결
    console.log(`Funding ${fundingId} cancelled`);
  };

  const handleComplete = (fundingId) => {
    // API 연결
    console.log(`Funding ${fundingId} completed`);
  };

  return (
    <>
      <div className="myartistfunding-container">
        <div className="myartistfunding-funding">소속 아티스트 펀딩</div>
        <div className="myartistfunding-funding-list">
          {fundingData.map((funding) => (
            <div
              className="myartistfunding-funding-item"
              key={funding.fundingId}
            >
              <FundingCard funding={funding} />
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default MyArtistFunding;
