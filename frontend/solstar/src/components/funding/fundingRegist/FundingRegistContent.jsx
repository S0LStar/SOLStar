import MyEditor from '../../common/MyEditor';
import './FundingRegistContent.css';

function FundingRegistContent() {
  return (
    <form className="funding-regist-content-section">
      <div className="funding-regist-content-description">펀딩 계획 작성</div>
      <MyEditor />
    </form>
  );
}

export default FundingRegistContent;
