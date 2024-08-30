import './LoginContainer.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import AxiosInstance from '../../util/AxiosInstance.js';
import Shoo from '../../assets/character/Shoo.png';
import WideButton from '../common/WideButton';
import { setToken } from '../../redux/slices/authSlice'; // Redux 액션 import

function LoginContainer() {
  const navigate = useNavigate();
  const dispatch = useDispatch(); // Redux 디스패치 사용
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isAgency, setIsAgency] = useState(false); // 소속사 체크 상태

  const handleSubmit = async (e) => {
    e.preventDefault();

    // 이메일과 비밀번호 유효성 검사
    if (email.length < 8 || password.length < 8) {
      alert('이메일과 비밀번호는 최소 8글자 이상이어야 합니다.');
      return;
    }

    const role = isAgency ? 'AGENCY' : 'USER'; // 체크박스 상태에 따라 role 설정

    try {
      const response = await AxiosInstance.post('/auth/login', {
        role: role,
        email: email,
        password: password,
      });

      // 로그인 성공 시 accessToken과 refreshToken을 Redux에 저장
      const { accessToken, refreshToken } = response.data.data;
      dispatch(setToken({ accessToken, refreshToken }));

      alert('로그인 성공');
      navigate('/');
    } catch (error) {
      console.error('로그인 실패:', error);
      alert('로그인 실패');
    }
  };

  return (
    <div className="login-container">
      <div className="login-logo">
        <img src={Shoo} alt="Logo" />
      </div>
      <form className="login-form" onSubmit={handleSubmit}>
        <div className="login-box">
          <div className="login-email">
            <label className="login-label" htmlFor="email">
              이메일
            </label>
            <label className="login-label">
              <input
                type="checkbox"
                checked={isAgency}
                onChange={(e) => setIsAgency(e.target.checked)}
              />
              &nbsp;소속사
            </label>
          </div>
          <input
            className="login-input"
            type="email"
            id="email"
            name="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            minLength="8"
          />
        </div>
        <div className="login-box">
          <label className="login-label" htmlFor="password">
            비밀번호
          </label>
          <input
            className="login-input"
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength="8"
          />
        </div>
        <div className="login-box"></div>
        <WideButton isActive={true} onClick={handleSubmit}>
          로그인
        </WideButton>
      </form>

      <div className="login-signup-link-container">
        <span className="login-signup-link" onClick={() => navigate('/signup')}>
          회원가입
        </span>
      </div>
    </div>
  );
}

export default LoginContainer;
