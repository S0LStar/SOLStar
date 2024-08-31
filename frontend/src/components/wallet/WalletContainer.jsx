import './WalletContainer.css';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import AxiosInstance from '../../util/AxiosInstance'; // AxiosInstance 임포트
import WalletItem from './WalletItem.jsx';
import FundingWalletItem from './FundingWalletItem.jsx';
import Loading from '../common/Loading';

function WalletContainer() {
  const navigate = useNavigate();
  const [walletData, setWalletData] = useState(null); // 기존 상태
  const [createWalletData, setCreateWalletData] = useState([]); // 새 상태 추가
  const [loading, setLoading] = useState(true); // 로딩 상태
  const [error, setError] = useState(null); // 에러 상태

  useEffect(() => {
    const fetchWalletData = async () => {
      try {
        const response = await AxiosInstance.get('wallet/my');
        const responseData = response.data.data;

        // 필요한 데이터만 추출하고 새로운 객체를 만들어 상태 업데이트
        const processedWalletData = {
          balance: responseData.accountBalance,
          accountNo: responseData.accountNo,
          name: responseData.userName,
          code: '신한', // 고정값 설정
        };

        setWalletData(processedWalletData); // 상태 업데이트
      } catch (error) {
        console.error('지갑 데이터를 가져오는 데 실패했습니다:', error);
        setError('지갑 데이터를 가져오는 데 실패했습니다.');
      } finally {
        setLoading(false); // 데이터 로드 완료 후 로딩 상태 업데이트
      }
    };

    const fetchCreateWalletData = async () => {
      try {
        const response = await AxiosInstance.get('wallet/my/host-funding');
        const responseData = response.data.data;

        // 리스트 형태의 데이터를 처리
        const processedCreateWalletData = responseData.map((item) => ({
          id: item.fundingId, // 아이디를 key로 사용
          accountNo: item.accountNo,
          balance: item.accountBalance,
          artistName: item.artistName,
          userName: item.userName,
          code: '신한', // 고정값 설정
        }));

        setCreateWalletData(processedCreateWalletData); // 상태 업데이트
      } catch (error) {
        console.error('펀딩 지갑 데이터를 가져오는 데 실패했습니다:', error);
        setError('펀딩 지갑 데이터를 가져오는 데 실패했습니다.');
      }
    };

    fetchWalletData();
    fetchCreateWalletData(); // 추가 요청 실행
  }, []);

  if (loading) {
    return (
      <div>
        <Loading>
          <span style={{ color: '#0046ff' }}>정산 내역</span>을<br /> 가져오는
          중이에요
        </Loading>
      </div>
    ); // 로딩 중일 때 표시
  }

  if (error) {
    return <div>{error}</div>; // 에러가 발생했을 때 표시
  }

  return (
    <>
      <div className="wallet-container">
        <div className="wallet-header">나의 지갑</div>
        {walletData ? (
          <WalletItem walletData={walletData} />
        ) : (
          <div className="nowallet">지갑 데이터가 없습니다.</div>
        )}

        <div className="wallet-header">펀딩 지갑</div>
        {createWalletData.length > 0 ? (
          createWalletData.map((wallet) => (
            <FundingWalletItem
              key={wallet.id}
              walletData={wallet}
              onClick={() =>
                navigate(`/wallet/${wallet.id}`, {
                  state: { walletData: wallet },
                })
              }
            />
          ))
        ) : (
          <div className="nowallet">펀딩 지갑 데이터가 없습니다.</div>
        )}
      </div>
    </>
  );
}

export default WalletContainer;
