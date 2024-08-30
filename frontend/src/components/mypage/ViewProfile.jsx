import './ViewProfile.css';
import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import WideButton from '../common/WideButton';
import temp from '../../assets/character/Shoo.png';
import LeftVector from '../../assets/common/LeftVector.png';

function ViewProfile() {
  const navigate = useNavigate();
  const location = useLocation(); // **useLocation을 통해 전달된 state 가져오기**
  const { profileData } = location.state || {}; // **state에서 profileData 가져오기**

  return (
    <>
      <div className="view-container">
        <div className="view-header">
          <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />내
          정보 수정
        </div>
        <img
          className="view-myprofile-img"
          src={profileData.img}
          alt="프로필 이미지"
        />
        <div className="view-contents">
          <div className="view-content">
            <div className="view-label">E-mail</div>
            <input
              className="view-input"
              type="text"
              value={profileData.email}
              disabled
            />
          </div>
          <div className="view-content">
            <div className="view-label">이름</div>
            <input
              className="view-input"
              type="text"
              value={profileData.name}
              disabled
            />
          </div>
          <div className="view-content">
            <div className="view-label">닉네임</div>
            <input
              className="view-input"
              type="text"
              value={profileData.nickname}
              disabled
            />
          </div>
          <div className="view-content">
            <div className="view-label">생년월일</div>
            <input
              className="view-input"
              type="text"
              value={profileData.birthdate}
              placeholder="yyyy.MM.dd"
              disabled
            />
          </div>
          <div className="view-content">
            <div className="view-label">전화번호</div>
            <input
              className="view-input"
              type="text"
              value={profileData.phone}
              disabled
            />
          </div>
        </div>
        <div className="view-buttons">
          <WideButton onClick={() => navigate(-1)} isActive={false}>
            취소
          </WideButton>
          <WideButton onClick={() => navigate(`/my`)} isActive={true}>
            완료
          </WideButton>
        </div>
      </div>
    </>
  );
}

export default ViewProfile;
