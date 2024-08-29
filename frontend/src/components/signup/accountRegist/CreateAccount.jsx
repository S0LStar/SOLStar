import './CreateAccount.css';
import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import DefaultImage from '../../../assets/funding/DefaultImage.png';

function CreateAccount() {
  const navigate = useNavigate();
  const [currentStep] = useState(2);
  const [account, setAccount] = useState({
    email: '',
    name: '',
    nickname: '',
    birthdate: '',
    phone: '',
    profileImage: '',
  });
  const [nextActive, setNextActive] = useState(false); // 다음 버튼 활성화 상태
  const [previewImage, setPreviewImage] = useState(null); // 이미지 미리보기 상태

  const fileInputRef = useRef(null); // 파일 입력 요소 참조 생성

  // 모든 필드가 채워졌는지 확인 (프로필 이미지는 필수가 아님)
  useEffect(() => {
    const isFormComplete = Boolean(
      account.email &&
        account.name &&
        account.nickname &&
        account.birthdate &&
        account.phone
      // 프로필 이미지를 제외한 필드만 확인
    );

    setNextActive(isFormComplete);
  }, [account]);

  // Input change handler
  const handleChange = (e) => {
    const { id, value } = e.target;
    setAccount((prevAccount) => ({
      ...prevAccount,
      [id]: value,
    }));
  };

  // 이미지 업로드 클릭 핸들러
  const handleImageUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  // 이미지 변경 핸들러
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setPreviewImage(URL.createObjectURL(file));
      setAccount((prevAccount) => ({
        ...prevAccount,
        profileImage: file,
      }));
    }
  };

  return (
    <>
      <div className="create-container">
        <div className="create-header">
          <div className="create-header-backInfo">
            <img src={LeftVector} alt="뒤로가기" onClick={() => navigate(-1)} />
            회원 정보 등록
          </div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <form className="create-form">
          <div className="create-form-image">
            {/* <label>프로필 이미지 (선택 사항)</label> */}
            <div
              onClick={handleImageUploadClick}
              className="create-image-preview"
            >
              {previewImage ? (
                <img src={previewImage} alt="미리보기" />
              ) : (
                <img src={DefaultImage} alt="기본 이미지" />
              )}
            </div>
            <input
              type="file"
              ref={fileInputRef}
              accept="image/*"
              onChange={handleImageChange}
              style={{ display: 'none' }}
            />
          </div>
          <div className="create-form-field">
            <input
              type="email"
              id="email"
              value={account.email}
              onChange={handleChange}
              placeholder="이메일"
            />
          </div>
          <div className="create-form-field">
            <input
              type="text"
              id="name"
              value={account.name}
              onChange={handleChange}
              placeholder="이름"
            />
          </div>
          <div className="create-form-field">
            <input
              type="text"
              id="nickname"
              value={account.nickname}
              onChange={handleChange}
              placeholder="닉네임"
            />
          </div>
          <div className="create-form-field">
            <input
              type="date"
              id="birthdate"
              value={account.birthdate}
              onChange={handleChange}
              placeholder="생년월일"
            />
          </div>
          <div className="create-form-field">
            <input
              type="text"
              id="phone"
              value={account.phone}
              onChange={handleChange}
              placeholder="전화번호"
            />
          </div>
        </form>

        <WideButton
          isActive={nextActive}
          onClick={() => {
            if (nextActive) {
              navigate('/signup/regist', { state: { account } });
            }
          }}
        >
          다음
        </WideButton>
      </div>
    </>
  );
}

export default CreateAccount;
