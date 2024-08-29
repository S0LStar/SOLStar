import { useEffect, useState } from 'react';

function FundingPlan({ fundingId }) {
  const [fundingPlan, setFundingPlan] = useState('');

  useEffect(() => {
    // TODO : 펀딩 계획 내용 API 연결 (/api/funding/content/{fundingId})
    // 펀딩 계획 내용 Data
    const fetchContent = {
      content: `<h2>Welcome to React-Quill</h2>
    <p>This is an <strong>example</strong> of a text editor.</p>
    <ul>
      <li>Item 1</li>
      <li>Item 2</li>
      <li>Item 3</li>
    </ul>`,
    };

    setFundingPlan(fetchContent.content);
  }, []);

  return (
    <div
      dangerouslySetInnerHTML={{ __html: fundingPlan }}
      style={{ padding: '10px', marginTop: '15px' }}
    />
  );
}

export default FundingPlan;
