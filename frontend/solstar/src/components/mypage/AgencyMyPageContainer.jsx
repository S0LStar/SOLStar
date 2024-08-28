import './AgencyMyPageContainer.css';
import { useNavigate } from 'react-router-dom';
import temp from '../../assets/character/Shoo.png';
import WideButton from '../common/WideButton';
import RightVector from '../../assets/common/RightVector.png';
import Pencil from '../../assets/mypage/Pencil.png';

function AgencyMyPageContainer() {
  const navigate = useNavigate();

  const myProfileTempData = {
    img: temp,
    name: '어도어',
    email: '',
    nickname: '',
    birthdate: '',
    phone: '',
  };

  return (
    <>
      <div className="agencymy-container">
        <img className="agencymy-profile" src={myProfileTempData.img} alt="" />
        <div className="agencymy-nickname">{myProfileTempData.name}</div>

        <div className="agencymy-edit-button">
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

        <div className="agencymy-funding">
          <div className="agencymy-funding-label">소속사</div>
          <hr />
          <div
            className="agencymy-funding-container"
            onClick={() => {
              navigate(`/agencymy/request`);
            }}
          >
            <div className="agencymy-funding-context">
              인증 펀딩 요청
              <img src={RightVector} alt="" />
            </div>
            <div
              className="agencymy-funding-context"
              onClick={() => {
                navigate(`/agencymy/myartist`);
              }}
            >
              소속 아티스트 <img src={RightVector} alt="" />
            </div>
            <div
              className="agencymy-funding-context"
              onClick={() => {
                navigate(`/agencymy/myartistfunding`);
              }}
            >
              소속 아티스트 펀딩 <img src={RightVector} alt="" />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default AgencyMyPageContainer;
