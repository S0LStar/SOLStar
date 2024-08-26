import propTypes from 'prop-types';
import MyEditor from '../../common/MyEditor';
import WideButton from '../../common/WideButton';
import './FundingRegistContent.css';
import { useEffect, useState } from 'react';

function FundingRegistContent({ funding, onChange, onRegist }) {
  const [registActive, setRegistActive] = useState(false);

  useEffect(() => {
    console.log('isActive', registActive);
  });
  return (
    <>
      <form className="funding-regist-content-section">
        <div className="funding-regist-content-description">펀딩 계획 작성</div>
        <MyEditor
          funding={funding}
          onChange={onChange}
          setRegistActive={setRegistActive}
        />
      </form>
      <WideButton onClick={onRegist} isActive={registActive}>
        등록
      </WideButton>
    </>
  );
}

FundingRegistContent.propTypes = {
  funding: propTypes.object.isRequired,
  onChange: propTypes.func.isRequired,
  onRegist: propTypes.func.isRequired,
};

export default FundingRegistContent;
