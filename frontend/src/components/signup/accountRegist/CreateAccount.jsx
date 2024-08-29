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
    password: '',
    passwordConfirm: '',
  });
  const [nextActive, setNextActive] = useState(false); // 다음 넘어가기 전 검증
  const [previewImage, setPreviewImage] = useState(null); // 이미지

  const fileInputRef = useRef(null); //파일 입출력

  // 이미지 빼고 모두 입력시 다음 가능
  useEffect(() => {
    const isFormComplete = Boolean(
      account.email &&
        account.name &&
        account.nickname &&
        account.birthdate &&
        account.phone &&
        account.password &&
        account.passwordConfirm &&
        account.password === account.passwordConfirm // 비밀번호 확인
    );

    setNextActive(isFormComplete);
  }, [account]);

  // 입력 시 변경
  const handleChange = (e) => {
    const { id, value } = e.target;
    setAccount((prevAccount) => ({
      ...prevAccount,
      [id]: value,
    }));
  };

  // 클릭 시 사진 입력
  const handleImageUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  // 이미지 변경
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
              type="password"
              id="password"
              value={account.password}
              onChange={handleChange}
              placeholder="비밀번호"
            />
          </div>
          <div className="create-form-field">
            <input
              type="password"
              id="passwordConfirm"
              value={account.passwordConfirm}
              onChange={handleChange}
              placeholder="비밀번호 확인"
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
