import './MyContainer.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import temp from '../../assets/character/Shoo.png';
import WideButton from '../common/WideButton';
import RightVector from '../../assets/common/RightVector.png';
import Pencil from '../../assets/mypage/Pencil.png';
import AxiosInstance from '../../util/AxiosInstance';

function MyContainer() {
  const navigate = useNavigate();
  const [profileData, setProfileData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    // 컴포넌트가 마운트될 때 API 호출
    const fetchProfile = async () => {
      try {
        const response = await AxiosInstance.get('/user/me');
        console.log('프로필 데이터:', response.data);
        setProfileData(response.data);
      } catch (error) {
        console.error('프로필 불러오기 실패:', error);
        setError('프로필 불러오기 실패');
        alert('로그인 실패');
      }
    };

    fetchProfile();
  }, []); // 빈 배열: 컴포넌트가 마운트될 때만 실행

  const myProfileTempData = profileData || {
    img: temp,
    name: '최승탁',
    introduction: '',
    email: '',
    nickname: '',
    birthdate: '',
    phone: '',
  };

  return (
    <>
      <div className="my-container">
        <img className="my-profile" src={myProfileTempData.img} alt="" />
        <div className="my-nickname">{myProfileTempData.name}</div>

        <div className="my-intro">
          <div className="my-intro-text">
            {myProfileTempData.introduction || '자기소개를 입력해주세요'}
            <img src={Pencil} alt="" />
          </div>
          <hr />
        </div>
        <div className="my-edit-button">
          <WideButton
            onClick={() => {
              navigate(`/my/editprofile`);
            }}
            isActive={true}
          >
            내 정보 수정
          </WideButton>
          <WideButton
            onClick={() => {
              navigate(`/my/editpassword`);
            }}
            isActive={false}
          >
            비밀번호 수정
          </WideButton>
        </div>

        <div className="my-funding">
          <div className="my-funding-label">내 펀딩</div>
          <hr />
          <div className="my-funding-container">
            <div
              className="my-funding-context"
              onClick={() => {
                navigate(`/my/participantfunding`);
              }}
            >
              내 참여 펀딩
              <img src={RightVector} alt="" />
            </div>
            <div
              className="my-funding-context"
              onClick={() => {
                navigate(`/my/createdfunding`);
              }}
            >
              내 주최 펀딩 <img src={RightVector} alt="" />
            </div>
          </div>
        </div>
        <button onClick={() => navigate('/login')}>
          <span>로그인</span>
        </button>
        <button onClick={() => navigate('/agencymy')}>
          <span>소속사 마이페이지</span>
        </button>
      </div>
    </>
  );
}

export default MyContainer;
