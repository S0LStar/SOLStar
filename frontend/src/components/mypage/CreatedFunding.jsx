import './CreatedFunding.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import WideButton from '../common/WideButton';
import temp from '../../assets/character/Shoo.png';
import LeftVector from '../../assets/common/LeftVector.png';

import FundingCard from '../funding/common/FundingCard';
import axiosInstance from '../../util/AxiosInstance';
import BackButton from '../common/BackButton';
import Empty from '../common/Empty';

function CreatedFunding() {
  const navigate = useNavigate();
  const [fundingData, setFundingData] = useState([]);

  const fetchCreatedFundingData = async () => {
    const response = await axiosInstance.get('user/host-funding');
    console.log('1', response);
    setFundingData(response.data.data);
    // const fundingData = response.data;
  };

  useEffect(() => {
    fetchCreatedFundingData();
  }, []);

  // const fundingData = [
  //   {
  //     fundingId: 1,
  //     type: 'VERIFIED',
  //     artistName: '뉴진스',
  //     title: '뉴진스 데뷔 2주년 기념🎉 2호선을 뉴진스로 물들여요!',
  //     fundingImage: 'image',
  //     successRate: 372,
  //     totalAmount: 18600000,
  //     status: 'PROCESSING',
  //     remainDays: 22,
  //   },
  //   {
  //     fundingId: 2,
  //     type: 'COMMON',
  //     artistName: '민지 (NewJeans)',
  //     title:
  //       '뉴진스 민지의 이름으로 따뜻한 마음을 전해요 💙 펀딩이 함께하는 사랑의 기부',
  //     fundingImage: '../../../assets/character/Sol.png',
  //     successRate: 160,
  //     totalAmount: 1600000,
  //     status: 'SUCCESS',
  //     remainDays: null,
  //   },
  //   {
  //     fundingId: 3,
  //     type: 'COMMON',
  //     artistName: '뉴진스',
  //     title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
  //     fundingImage: 'funding_image_3_url',
  //     successRate: 50,
  //     totalAmount: 1230000,
  //     status: 'FAIL',
  //     remainDays: null,
  //   },
  //   {
  //     fundingId: 4,
  //     type: 'COMMON',
  //     artistName: '뉴진스',
  //     title: '뉴진스 한정판 굿즈 💖 팬심으로 만든 특별 아이템',
  //     fundingImage: 'funding_image_3_url',
  //     successRate: 50,
  //     totalAmount: 1230000,
  //     status: 'FAIL',
  //     remainDays: null,
  //   },
  // ];

  return (
    <>
      <div className="createdfunding-container">
        <div className="createdfunding-funding">
          <BackButton />
          <div className="createdfunding-header-description">내 주최 펀딩</div>
        </div>
        <div className="createdfunding-funding-list">
          {fundingData.length === 0 ? (
            <Empty>내 주최 펀딩</Empty>
          ) : (
            fundingData.map((funding) => (
              <div
                className="createdfunding-funding-item"
                key={funding.fundingId}
              >
                <FundingCard funding={funding} />
              </div>
            ))
          )}
        </div>
      </div>
    </>
  );
}

export default CreatedFunding;
