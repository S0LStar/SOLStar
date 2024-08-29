import { useEffect } from 'react';
import { useParams } from 'react-router-dom';

function FundingPayment() {
  const { fundingId } = useParams();

  useEffect(() => {
    // TODO : 펀딩 계좌 상세 조회 API 연결 (/api/wallet/funding-detail)
    // request body : fundingId
    const fetchFundingPaymnet = {
      accountNo: 1,
      userName: '뉴진스 성덕',
      accountBalance: 1500000,
      list: [],
    };
  });

  return <div>FundingPayment</div>;
}

export default FundingPayment;
