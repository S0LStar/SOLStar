import { useEffect } from 'react';

import BackButton from '../../common/BackButton';

function FundingNoticeRegist() {
  useEffect(() => {
    console.log('notice-regist');
  });
  return (
    <div className="funding-notice-regist-container">
      <header>
        <BackButton />
        <div>어떤 공지글 인가요 ?</div>
      </header>
      <div className="funding-notice-form-container">
        <form></form>
      </div>
    </div>
  );
}

export default FundingNoticeRegist;
