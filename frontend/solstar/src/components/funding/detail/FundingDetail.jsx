import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import WideButton from '../../common/WideButton';
import './FundingDetail.css';

import Sol from '../../../assets/character/Sol.png'; // temp Image
import Certification from '../../../assets/common/Certification.png';

function FundingDetail() {
  const location = useLocation();
  const [funding, setFunding] = useState(null);

  useEffect(() => {
    // TODO : API 연결
    // 펀딩 상세 조회 Data
    const tempData = {
      title: '🎉 뉴진스 데뷔 2주년 기념 🎉 2호선을 뉴진스로 물들여요!',
      artistProfileImage: 'image',
      artistName: '뉴진스',
      fundingImage: 'image_url2',
      hostNickname: '뉴진스찐팬',
      hostIntroduction: '뉴진스 찐팬 2년차입니다.',
      hostProfileImage: null,
      totalAmount: 500000,
      goalAmount: 1000000,
      deadlineDate: '2024-08-28',
      totalJoin: 1,
      type: 'VERIFIED',
      status: 'PROCESSING', // PROCESSING, SUCCESS, FAIL, CLOSED
      joinable: true, // true: 참여 가능 (펀딩 진행 중 && 펀딩 참여 전 && 주최자 x), false: 참여 불가능 (그 외의 모든 경우) => 여러번 참여 가능하다면 상태값 처리 수정
    };

    setFunding(tempData);
  }, [location]);

  if (!funding) {
    return <div>Loading...</div>;
  }

  const isVerified = funding.type === 'VERIFIED';
  const progressPercentage = Math.floor(
    (funding.totalAmount / funding.goalAmount) * 100
  );

  const handleJoin = () => {
    console.log('펀딩 참여!');
  };

  return (
    <div className="funding-detail-container">
      <img src={Sol} alt="Funding" className="funding-detail-image" />
      <div className="funding-detail-header">
        {isVerified && (
          <div className="certification-header">
            <img
              src={Certification}
              alt=""
              className="funding-certification-mark"
            />
            <div>공식 인증 펀딩</div>
          </div>
        )}
        <h1 className="funding-detail-title">{funding.title}</h1>

        <div className="funding-detail-artist-info">
          <img src={Certification} alt="" />
          <div>{funding.artistName}</div>
        </div>
        <div
          className={`funding-detail-info ${
            funding.status === 'PROCESSING'
              ? 'progress'
              : funding.status === 'SUCCESS' || funding.status === 'CLOSED'
                ? 'success'
                : 'fail'
          }`}
          style={{ '--achievement-percentage': progressPercentage }}
        >
          <div className="funding-detail-progress">
            <div>
              <div className="funding-detail-amount">
                <div>모인 금액</div>
                <div>{funding.totalAmount.toLocaleString()}원</div>
              </div>
              <div className="funding-detail-join-count">
                {funding.totalJoin.toLocaleString()}명 참여
              </div>
            </div>
            <div>{progressPercentage}%</div>
          </div>
          <div className="funding-card-achievement-bar"></div>
          <div className="funding-detail-deadline">
            <div>
              <div>펀딩 목표액</div>
              <div>{funding.goalAmount.toLocaleString()}원</div>
            </div>
            <div>
              <div>펀딩 마감일</div>
              <div>{funding.deadlineDate}</div>
            </div>
          </div>
          <div className="funding-detail-host">
            <div className="funding-detail-host-title">주최자 소개</div>
            <div>
              <img
                src={Certification}
                alt=""
                className="funding-detail-host-profile"
              />
              <div>
                <div className="funding-detail-host-name">
                  {funding.hostNickname}
                </div>
                {funding.hostIntroduction && (
                  <div className="funding-detail-host-description">
                    {funding.hostIntroduction}
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="funding-content-container">
        <div className="funding-content-tab"></div>
        <div className="funding-content-detail"></div>
      </div>

      {funding.joinable && (
        <div className="wide-button-fix">
          <WideButton onClick={handleJoin} isActive={true}>
            펀딩 참여하기
          </WideButton>
        </div>
      )}
    </div>
  );
}

export default FundingDetail;
