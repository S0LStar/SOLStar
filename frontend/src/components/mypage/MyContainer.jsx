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
  const [nickname, setNickname] = useState('');
  const [isEditingIntroduction, setIsEditingIntroduction] = useState(false); // 자기소개 수정 모드 상태 추가
  const [isEditingNickname, setIsEditingNickname] = useState(false); // 닉네임 수정 모드 상태 추가
  const [error, setError] = useState(null);

  useEffect(() => {
    // 컴포넌트가 마운트될 때 API 호출
    if (authState.role === 'AGENCY') {
      // 여기에 소속사일 경우 fetchprofile 해야 함
    } else {
      const fetchProfile = async () => {
        try {
          const response = await AxiosInstance.get('/user/me');
          console.log('프로필 데이터:', response.data.data);
          setProfileData(response.data.data);
          setIntroduction(response.data.data.introduction || '');
          setNickname(response.data.data.nickname || '');
        } catch (error) {
          console.error('프로필 불러오기 실패:', error);
          setError('프로필 불러오기 실패');
          alert('로그인 실패');
        }
      };
      fetchProfile();
    }
  }, [authState.role]); // 빈 배열: 컴포넌트가 마운트될 때만 실행

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

  const handleNicknameChange = (e) => {
    const newNickname = e.target.value;
    if (newNickname.length > 10) {
      alert('닉네임은 최대 10자까지 가능합니다.');
    } else {
      setNickname(newNickname);
    }
  };

  const handleIntroductionUpdate = async () => {
    if (isEditingIntroduction) {
      // 현재 자기소개 수정 모드이면 수정 내용을 저장
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
    setIsEditingIntroduction(!isEditingIntroduction); // 자기소개 수정 모드를 토글
  };

  const handleNicknameUpdate = async () => {
    if (isEditingNickname) {
      // 현재 닉네임 수정 모드이면 수정 내용을 저장
      try {
        const response = await AxiosInstance.patch('/user/nickname', {
          nickname,
        });
        console.log('닉네임 업데이트 성공:', response.data);
        alert('닉네임이 업데이트되었습니다.');
      } catch (error) {
        console.error('닉네임 업데이트 실패:', error);
        alert('닉네임 업데이트 실패');
      }
    }
    setIsEditingNickname(!isEditingNickname); // 닉네임 수정 모드를 토글
  };

  const handleLogout = async () => {
    try {
      // 서버에 로그아웃 요청
      await AxiosInstance.post('/user/logout');

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
    name: authState.role === 'AGENCY' ? '어도어' : '',
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

        {authState.role === 'AGENCY' ? (
          // AGENCY의 경우
          <>
            <div className="my-name">{myProfileTempData.name}</div>
            <div className="my-edit-button">
              <WideButton
                onClick={() => {
                  navigate(`/my`);
                }}
                isActive={true}
              >
                내 정보 보기
              </WideButton>
              <WideButton onClick={handleLogout} isActive={false}>
                로그아웃
              </WideButton>
            </div>
            <div className="my-funding">
              <div className="my-funding-label">소속사</div>
              <hr />
              <div className="my-funding-container">
                <div
                  className="my-funding-context"
                  onClick={() => {
                    navigate(`/agencymy/request`);
                  }}
                >
                  인증 펀딩 요청
                  <img src={RightVector} alt="" />
                </div>
                <div
                  className="my-funding-context"
                  onClick={() => {
                    navigate(`/agencymy/myartist`);
                  }}
                >
                  소속 아티스트 <img src={RightVector} alt="" />
                </div>
                <div
                  className="my-funding-context"
                  onClick={() => {
                    navigate(`/agencymy/myartistfunding`);
                  }}
                >
                  소속 아티스트 펀딩 <img src={RightVector} alt="" />
                </div>
              </div>
            </div>
          </>
        ) : (
          // 일반 USER의 경우
          <>
            <div className="my-name">
              <div className="my-name-text">
                {isEditingNickname ? (
                  <input
                    type="text"
                    value={nickname}
                    onChange={handleNicknameChange}
                    placeholder="닉네임을 입력해주세요"
                  />
                ) : (
                  <span>{nickname || '닉네임을 입력해주세요'}</span>
                )}
                <img src={Pencil} alt="Edit" onClick={handleNicknameUpdate} />
              </div>
            </div>
            <div className="my-intro">
              <div className="my-intro-text">
                {isEditingIntroduction ? (
                  <input
                    type="text"
                    value={introduction}
                    onChange={handleIntroductionChange}
                    placeholder="자기소개를 입력해주세요"
                  />
                ) : (
                  <span>{introduction || '자기소개를 입력해주세요'}</span>
                )}
                <img
                  src={Pencil}
                  alt="Edit"
                  onClick={handleIntroductionUpdate}
                />
              </div>
              <hr />
            </div>
            <div className="my-edit-button">
              <WideButton
                onClick={() => {
                  navigate(`/my/ViewProfile`, { state: { profileData } });
                }}
                isActive={true}
              >
                내 정보 보기
              </WideButton>
              <WideButton onClick={handleLogout} isActive={false}>
                로그아웃
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
          </>
        )}
      </div>
    </>
  );
}

export default MyContainer;
