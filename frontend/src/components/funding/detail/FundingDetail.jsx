import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import WideButton from '../../common/WideButton';
import BackButton from '../../common/BackButton';
import './FundingDetail.css';
import FundingPlan from './FundingPlan';
import FundingNoti from './FundingNotiList';
import FundingJoinModal from './FundingJoinModal';
import FundingPayment from './FundingPayment';

import Sol from '../../../assets/character/Sol.png'; // temp Image
import Certification from '../../../assets/common/Certification.png';
import Success from '../../../assets/funding/Success.png';
import Fail from '../../../assets/funding/Fail.png';
import Closed from '../../../assets/funding/Closed.png';

function FundingDetail() {
  const { fundingId } = useParams();
  const [funding, setFunding] = useState(null);
  const [activeTab, setActiveTab] = useState('plan'); // 활성화 탭 상태관리
  const [joinModalOpen, setJoinModalOpen] = useState(false); // 펀딩 참여 모달 상태 관리

  useEffect(() => {
    console.log(parseInt(fundingId));

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
      joinStatus: 2, // 0: 참여 가능 대상 (펀딩 진행 중 && 펀딩 참여 전 && 주최자 x), 1: 참여자, 2: 주최자 => 여러번 참여 가능하다면 상태값 수정 필요
    };

    setFunding(tempData);
  }, [location]);

  if (!funding) {
    return <div>Loading...</div>;
  }

  const progressPercentage = Math.floor(
    (funding.totalAmount / funding.goalAmount) * 100
  );

  const handleJoin = () => {
    console.log('펀딩 참여!');
    setJoinModalOpen(true);
  };

  const closeModal = () => {
    setJoinModalOpen(false);
  };

  const handleFinish = () => {
    // TODO: 정산 종료 API 요청
  };

  return (
    <div
      className={`funding-detail-container ${funding.joinStatus !== 0 && 'no-button'}`}
    >
      <div className="funding-detail-image-container">
        <BackButton />
        <img src={Sol} alt="Funding" className="funding-detail-image" />
      </div>
      <div className="funding-detail-header">
        {funding.type === 'VERIFIED' && (
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
        {funding.status !== 'PROCESSING' && (
          <div
            className={`funding-detail-status ${
              funding.status === 'SUCCESS'
                ? 'success'
                : funding.status === 'CLOSED'
                  ? 'closed'
                  : funding.status === 'FAIL'
                    ? 'fail'
                    : ''
            }`}
          >
            {funding.status === 'SUCCESS' ? (
              <img src={Success} alt="" className="funding-status-icon" />
            ) : funding.status === 'FAIL' ? (
              <img src={Fail} alt="" className="funding-status-icon" />
            ) : (
              <img src={Closed} alt="" className="funding-status-icon" />
            )}
            <div className="funding-detail-status-content">
              {funding.status === 'SUCCESS' ? (
                <div>
                  <div>펀딩 성공 !</div>
                  <div>축하해요 펀딩에 성공했어요</div>
                </div>
              ) : funding.status === 'FAIL' ? (
                <div>
                  <div>펀딩 무산</div>
                  <div>펀딩이 무산되었습니다</div>
                </div>
              ) : (
                <div>
                  <div>펀딩 종료</div>
                  <div>펀딩이 종료되었습니다</div>
                </div>
              )}
            </div>
          </div>
        )}
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
        <div className="funding-content-tab">
          <button
            onClick={() => setActiveTab('plan')}
            className={`funding-content-tab-button ${activeTab === 'plan' && 'active'}`}
          >
            프로젝트 계획
          </button>
          <button
            onClick={() => setActiveTab('noti')}
            className={`funding-content-tab-button ${activeTab === 'noti' && 'active'}`}
          >
            공지사항
          </button>
          {funding.joinStatus !== 0 && (
            <button
              onClick={() => setActiveTab('payment')}
              className={`funding-content-tab-button ${activeTab === 'payment' && 'active'}`}
            >
              정산
            </button>
          )}
        </div>
        <div className="funding-content-detail">
          {activeTab === 'plan' ? (
            <FundingPlan />
          ) : activeTab === 'noti' ? (
            <FundingNoti
              fundingId={parseInt(fundingId)}
              isHost={funding.joinStatus === 2}
            />
          ) : (
            funding.joinStatus !== 0 && (
              <FundingPayment
                artistName={funding.artistName}
                artistProfileImage={funding.artistProfileImage}
              />
            )
          )}
        </div>
      </div>

      {funding.joinStatus === 0 && (
        <div className="wide-button-fix">
          <WideButton onClick={handleJoin} isActive={true}>
            펀딩 참여하기
          </WideButton>
        </div>
      )}

      {funding.status === 'SUCCESS' && funding.joinStatus === 2 && (
        <div className="wide-button-fix">
          <WideButton onClick={handleFinish} isActive={true}>
            정산 종료하기
          </WideButton>
        </div>
      )}

      {joinModalOpen && (
        <FundingJoinModal
          isOpen={joinModalOpen}
          closeModal={closeModal}
          title={funding.title}
        />
      )}
    </div>
  );
}

export default FundingDetail;
