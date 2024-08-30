import './MyContainer.css';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux'; // Redux 디스패치 및 useSelector 사용
import { clearToken } from '../../redux/slices/authSlice'; // Redux 액션 import
import temp from '../../assets/character/Shoo.png';
import WideButton from '../common/WideButton';
import RightVector from '../../assets/common/RightVector.png';
import Pencil from '../../assets/mypage/Pencil.png';
import AxiosInstance from '../../util/AxiosInstance';
import DefaultArtist from '../../assets/common/DefaultArtist.png';

function MyContainer() {
  const navigate = useNavigate();
  const dispatch = useDispatch(); // Redux 디스패치 사용
  const authState = useSelector((state) => state.auth); // 현재 auth 상태 가져오기
  const [profileData, setProfileData] = useState(null);
  const [introduction, setIntroduction] = useState('');
  const [isEditing, setIsEditing] = useState(false); // 수정 모드 상태 추가
  const [error, setError] = useState(null);

  useEffect(() => {
    // 컴포넌트가 마운트될 때 API 호출
    const fetchProfile = async () => {
      try {
        const response = await AxiosInstance.get('/user/me');
        console.log('프로필 데이터:', response.data.data);
        setProfileData(response.data.data);
        setIntroduction(response.data.data.introduction || '');
      } catch (error) {
        console.error('프로필 불러오기 실패:', error);
        setError('프로필 불러오기 실패');
        alert('로그인 실패');
      }
    };

    fetchProfile();
  }, []); // 빈 배열: 컴포넌트가 마운트될 때만 실행

  const handleImageUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('profileImage', file);

    try {
      const response = await AxiosInstance.patch(
        '/user/me/profile-image',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }
      );
      console.log('프로필 이미지 업데이트 성공:', response.data);
      setProfileData((prevData) => ({
        ...prevData,
        img: URL.createObjectURL(file), // 로컬 이미지 URL 사용
      }));
    } catch (error) {
      console.error('프로필 이미지 업데이트 실패:', error);
      alert('프로필 이미지 업데이트 실패');
    }
  };

  const handleIntroductionChange = (e) => {
    const newIntroduction = e.target.value;
    if (newIntroduction.length > 20) {
      alert('자기소개는 최대 20자까지 가능합니다.');
    } else {
      setIntroduction(newIntroduction);
    }
  };

  const handleIntroductionUpdate = async () => {
    if (isEditing) {
      // 현재 수정 모드이면 수정 내용을 저장
      try {
        const response = await AxiosInstance.patch('/user/introduction', {
          introduction,
        });
        console.log('자기소개 업데이트 성공:', response.data);
        alert('자기소개가 업데이트되었습니다.');
      } catch (error) {
        console.error('자기소개 업데이트 실패:', error);
        alert('자기소개 업데이트 실패');
      }
    }
    setIsEditing(!isEditing); // 수정 모드를 토글
  };

  const handleLogout = async () => {
    try {
      // 서버에 로그아웃 요청
      await AxiosInstance.post('/auth/logout');

      // Redux 상태 초기화
      dispatch(clearToken());

      // 로그아웃 후 로그인 페이지로 이동
      navigate('/login');
    } catch (error) {
      console.error('로그아웃 실패:', error);
      alert('로그아웃 실패');
    }
  };

  const myProfileTempData = profileData || {
    img: temp,
    name: '',
    introduction: '',
    email: '',
    nickname: '',
    birthdate: '',
    phone: '',
  };

  return (
    <>
      <div className="my-container">
        <input
          type="file"
          id="profile-image-input"
          style={{ display: 'none' }}
          // onChange={handleImageUpload}
        />
        <img
          className="my-profile"
          src={myProfileTempData.img ? myProfileTempData.img : DefaultArtist}
          alt=""
          // onClick={() => document.getElementById('profile-image-input').click()}
        />
        <div className="my-nickname">{myProfileTempData.name}</div>

        <div className="my-intro">
          <div className="my-intro-text">
            {isEditing ? (
              <input
                type="text"
                value={introduction}
                onChange={handleIntroductionChange}
                placeholder="자기소개를 입력해주세요"
              />
            ) : (
              <span>{introduction || '자기소개를 입력해주세요'}</span>
            )}
            <img src={Pencil} alt="Edit" onClick={handleIntroductionUpdate} />
          </div>
          <hr />
        </div>
        <div className="my-edit-button">
          <WideButton
            onClick={() => {
              navigate(`/my/editprofile`, { state: { profileData } });
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
        <button onClick={handleLogout}>
          <span>로그아웃</span>
        </button>
      </div>
    </>
  );
}

export default MyContainer;
