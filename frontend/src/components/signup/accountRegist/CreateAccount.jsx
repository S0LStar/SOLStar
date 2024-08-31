import './CreateAccount.css';
import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import AxiosInstance from '../../../util/AxiosInstance'; // AxiosInstance 임포트
import ProgressBar from '../accountRegist/ProgressBar';
import LeftVector from '../../../assets/common/LeftVector.png';
import WideButton from '../../common/WideButton';
import DefaultImage from '../../../assets/funding/DefaultImage.png';
import SignUpButton from '../../common/SignUpButton';

function CreateAccount() {
  const API_LINK = import.meta.env.VITE_API_URL;
  const navigate = useNavigate();
  const [currentStep] = useState(2);
  const [account, setAccount] = useState({
    email: '',
    name: '',
    nickname: '',
    birthDate: '',
    phone: '',
    profileImage: '',
    password: '',
    passwordConfirm: '',
  });
  const [emailValid, setEmailValid] = useState(null); // 이메일 중복 체크 상태
  const [nicknameValid, setNicknameValid] = useState(null); // 닉네임 중복 체크 상태
  const [emailFormatValid, setEmailFormatValid] = useState(true); // 이메일 형식 체크 상태
  const [passwordMatch, setPasswordMatch] = useState(true); // 비밀번호 일치 상태
  const [nextActive, setNextActive] = useState(false); // 다음 넘어가기 전 검증
  const [previewImage, setPreviewImage] = useState(null); // 이미지

  const fileInputRef = useRef(null); // 파일 입출력

  useEffect(() => {
    const isFormComplete = Boolean(
      account.email &&
        account.name &&
        account.nickname &&
        account.birthDate &&
        account.phone &&
        account.password &&
        account.passwordConfirm &&
        account.password === account.passwordConfirm &&
        emailValid === true && // 이메일 중복 체크 완료
        emailFormatValid === true && // 이메일 형식 체크 완료
        nicknameValid === true && // 닉네임 중복 체크 완료
        passwordMatch === true // 비밀번호 일치 체크 완료
    );

    setNextActive(isFormComplete);
  }, [account, emailValid, emailFormatValid, nicknameValid, passwordMatch]);

  const handleChange = (e) => {
    const { id, value } = e.target;

    if (id === 'phone') {
      const formattedValue = value.replace(/[^0-9]/g, ''); // 숫자가 아닌 문자 제거
      if (formattedValue.length <= 11) {
        setAccount((prevAccount) => ({
          ...prevAccount,
          [id]: formattedValue,
        }));
      }
    } else if (id === 'name' || id === 'nickname') {
      if (value.length <= 10) {
        setAccount((prevAccount) => ({
          ...prevAccount,
          [id]: value,
        }));
      }
    } else {
      setAccount((prevAccount) => ({
        ...prevAccount,
        [id]: value,
      }));
    }

    if (id === 'email') {
      // 이메일 형식 체크
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      setEmailFormatValid(emailRegex.test(value));
    }

    if (id === 'password' || id === 'passwordConfirm') {
      // 비밀번호 중복 체크
      const newAccount = { ...account, [id]: value };
      setPasswordMatch(newAccount.password === newAccount.passwordConfirm);
    }
  };

  const handleBlur = async (e) => {
    const { id, value } = e.target;

    if (id === 'email' && emailFormatValid) {
      console.log(value);
      // email 중복 체크
      try {
        const response = await AxiosInstance.post(
          'auth/email/duplicate-check',
          { email: value }
        );
        console.log(response.data.data);
        setEmailValid(response.data.data.duplicate === false);
      } catch (error) {
        console.error('이메일 중복 체크 실패:', error);
        setEmailValid(false);
        setEmailValid(true);
      }
    }

    if (id === 'nickname') {
      // 닉네임 중복 체크
      try {
        const response = await AxiosInstance.post(
          'auth/nickname/duplicate-check',
          { nickname: value }
        );
        console.log(response.data.data);
        setNicknameValid(response.data.data.duplicate === false);
      } catch (error) {
        console.error('닉네임 중복 체크 실패:', error);
        setNicknameValid(false);
        setNicknameValid(true);
      }
    }
  };

  const handleImageUploadClick = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

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
          <SignUpButton />
          <div className="create-header-description">로그인 정보</div>
          <ProgressBar currentStep={currentStep} />
        </div>

        <form className="create-form">
          <div className="create-form-image">
            <div
              // onClick={handleImageUploadClick}
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
              onBlur={handleBlur}
              placeholder="이메일"
            />
            {emailFormatValid === false && (
              <div className="error-message">
                유효한 이메일 형식이 아닙니다.
              </div>
            )}
            {emailValid === false && (
              <div className="error-message">이미 사용 중인 이메일입니다.</div>
            )}
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
            {!passwordMatch && (
              <div className="error-message">비밀번호가 일치하지 않습니다.</div>
            )}
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
              onBlur={handleBlur}
              placeholder="닉네임"
            />
            {nicknameValid === false && (
              <div className="error-message">이미 사용 중인 닉네임입니다.</div>
            )}
          </div>
          <div className="create-form-field">
            <input
              type="date"
              id="birthDate"
              value={account.birthDate}
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
              placeholder="전화번호 01043214321"
            />
          </div>
        </form>

        <WideButton
          isActive={nextActive}
          onClick={() => {
            if (nextActive) {
              console.log(account);
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
