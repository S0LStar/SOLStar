import './RequestFunding.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import WideButton from '../common/WideButton';
import temp from '../../assets/character/Shoo.png';
import LeftVector from '../../assets/common/LeftVector.png';

import FundingCard from '../funding/common/FundingCard';

function RequestFunding() {
  const navigate = useNavigate();

  const fundingData = [
    {
      fundingId: 1,
      type: '',
      artistName: '뉴진스',
      title: '뉴진스 데뷔 2주년 기념🎉 2호선을 뉴진스로 물들여요!',
      fundingImage: 'image',
      successRate: '',
      totalAmount: '',
      status: '',
      remainDays: '',
    },
    {
      fundingId: 2,
      type: '',
      artistName: '민지 (NewJeans)',
      title:
        '뉴진스 민지의 이름으로 따뜻한 마음을 전해요 💙 펀딩이 함께하는 사랑의 기부',
      fundingImage: '../../../assets/character/Sol.png',
      successRate: '',
      totalAmount: '',
      status: '',
      remainDays: '',
    },
    {
      fundingId: 3,
      type: '',
      artistName: '뉴진스',
      title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
      fundingImage: 'funding_image_3_url',
      successRate: '',
      totalAmount: '',
      status: '',
      remainDays: '',
    },
    {
      fundingId: 4,
      type: '',
      artistName: '뉴진스',
      title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
      fundingImage: 'funding_image_3_url',
      successRate: '',
      totalAmount: '',
      status: '',
      remainDays: '',
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
      <div className="request-container">
        <div className="request-funding">인증 펀딩 요청</div>
        <div className="request-funding-list">
          {fundingData.map((funding) => (
            <div className="request-funding-item" key={funding.fundingId}>
              <FundingCard funding={funding} />
              <div className="request-funding-buttons">
                <WideButton
                  onClick={() => handleCancel(funding.fundingId)}
                  isActive={false}
                >
                  반려
                </WideButton>
                <WideButton
                  onClick={() => handleComplete(funding.fundingId)}
                  isActive={true}
                >
                  승인
                </WideButton>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default RequestFunding;
