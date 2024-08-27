import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function FundingDetail() {
  const location = useLocation();

  useEffect(() => {
    // console.log(location.)
  }, []);

  return <div>FundingDetail</div>;
}

export default FundingDetail;
