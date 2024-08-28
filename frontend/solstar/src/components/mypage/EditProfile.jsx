import './EditProfile.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import WideButton from '../common/WideButton';
import temp from '../../assets/character/Shoo.png';
import LeftVector from '../../assets/common/LeftVector.png';

function EditProfile() {
  const navigate = useNavigate();

  const [profileData, setProfileData] = useState({
    img: temp,
    name: '최승탁',
    introduction: '',
    email: 'user@example.com',
    nickname: 'nickname',
    birthdate: '2000.01.01',
    phone: '010-1234-5678',
  });

  const [birthdateError, setBirthdateError] = useState('');

  const handleChange = (field, value) => {
    setProfileData((prevData) => ({
      ...prevData,
      [field]: value,
    }));
  };

  const handleBlur = (field) => {
    if (field === 'birthdate') {
      if (!isValidDate(profileData.birthdate)) {
        setBirthdateError('올바른 날짜 형식으로 입력하세요. (예: 2000.01.01)');
      } else {
        setBirthdateError('');
      }
    }
  };

  const isValidDate = (dateString) => {
    const datePattern = /^\d{4}\.\d{2}\.\d{2}$/;
    if (!datePattern.test(dateString)) return false;

    const [year, month, day] = dateString.split('.').map(Number);
    const date = new Date(year, month - 1, day);
    return (
      date.getFullYear() === year &&
      date.getMonth() === month - 1 &&
      date.getDate() === day
    );
  };

  return (
    <>
      <div className="edit-container">
        <div className="edit-header">
          <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />내
          정보 수정
        </div>
        <img
          className="edit-myprofile-img"
          src={profileData.img}
          alt="프로필 이미지"
        />
        <div className="edit-contents">
          <div className="edit-content">
            <div className="edit-label">E-mail</div>
            <input
              className="edit-input"
              type="text"
              value={profileData.email}
              disabled
            />
          </div>
          <div className="edit-content">
            <div className="edit-label">이름</div>
            <input
              className="edit-input"
              type="text"
              value={profileData.name}
              onChange={(e) => handleChange('name', e.target.value)}
            />
          </div>
          <div className="edit-content">
            <div className="edit-label">닉네임</div>
            <input
              className="edit-input"
              type="text"
              value={profileData.nickname}
              onChange={(e) => handleChange('nickname', e.target.value)}
            />
          </div>
          <div className="edit-content">
            <div className="edit-label">생년월일</div>
            <input
              className="edit-input"
              type="text"
              value={profileData.birthdate}
              onChange={(e) => handleChange('birthdate', e.target.value)}
              onBlur={() => handleBlur('birthdate')}
              placeholder="yyyy.MM.dd"
            />
            {birthdateError && (
              <div className="error-message">{birthdateError}</div>
            )}
          </div>
          <div className="edit-content">
            <div className="edit-label">전화번호</div>
            <input
              className="edit-input"
              type="text"
              value={profileData.phone}
              onChange={(e) => handleChange('phone', e.target.value)}
            />
          </div>
        </div>
        <div className="edit-buttons">
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

export default EditProfile;
