import './MyContainer.css';
import { useNavigate } from 'react-router-dom';

function MyContainer() {
  const navigate = useNavigate();

  return (
    <>
      <div>Mypage Container</div>
      <button onClick={() => navigate('/login')}>
        <span>로그인</span>
      </button>
    </>
  );
}

export default MyContainer;
