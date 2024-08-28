import './MyContainer.css';
import { useNavigate } from 'react-router-dom';
import temp from '../../assets/character/Shoo.png';
import WideButton from '../common/WideButton';
import RightVector from '../../assets/common/RightVector.png';
import Pencil from '../../assets/mypage/Pencil.png';

function MyContainer() {
  const navigate = useNavigate();

  const myProfileTempData = {
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

        <div className="my-fundding">
          <div className="my-fundding-label">내 펀딩</div>
          <hr />
          <div
            className="my-fundding-container"
            onClick={() => {
              navigate(`/my/participantfundding`);
            }}
          >
            <div className="my-fundding-context">
              내 참여 펀딩
              <img src={RightVector} alt="" />
            </div>
            <div
              className="my-fundding-context"
              onClick={() => {
                navigate(`/my/createdfundding`);
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
