import { useEffect, useState } from 'react';
import axiosInstance from '../../../util/AxiosInstance';
import PropTypes from 'prop-types';

function FundingPlan({ fundingId }) {
  const [fundingPlan, setFundingPlan] = useState('');

  useEffect(() => {
    const fetchFundingContent = async () => {
      try {
        const response = await axiosInstance.get(
          `/funding/content/${fundingId}`
        );

        console.log(response);

        // 각 줄을 <p> 태그로 감싸고, 마진 바텀 추가
        const content = response.data.data.content
          .split('\n')
          .map((line) => `<p style="margin-bottom: 10px;">${line}</p>`)
          .join('');

        setFundingPlan(content);
      } catch (error) {
        console.error(error);
      }
    };
    fetchFundingContent();
  }, [fundingId]);

  return (
    <div
      dangerouslySetInnerHTML={{ __html: fundingPlan }}
      style={{ padding: '10px' }}
    />
  );
}

FundingPlan.propTypes = {
  fundingId: PropTypes.number.isRequired,
};

export default FundingPlan;
