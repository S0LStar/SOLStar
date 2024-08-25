import PropTypes from 'prop-types';
import WideButton from '../../common/WideButton';
import './FundingRegistInfo.css';

function FundingRegistInfo({ funding, onChange, onNext }) {
  return (
    <form className="funding-regist-section">
      {/* <div className="funding-regist-step">
        <div className="step-circle active">1</div>
        <div className="step-line"></div>
        <div className="step-circle">2</div>
      </div> */}

      <div className="funding-regist-type">
        <label htmlFor="type">펀딩 종류</label>
        <select
          type="select"
          id="type"
          value={funding.type}
          onChange={onChange}
        >
          <option value="COMMON">일반 펀딩</option>
          <option value="VERIFIED">인증 펀딩</option>
          <option value="ADVERTISE">홍보글</option>
        </select>
      </div>

      <div className="funding-regist-image">
        <label htmlFor="image">펀딩 대표 사진</label>
        <input
          type="file"
          id="image"
          onChange={(e) => {
            onChange({ target: { id: 'image', value: e.target.files[0] } });
          }}
        />
        <div className="funding-regist-image-description">
          <div>이런 사진을 등록해보세요 !</div>
          <div>- 아티스트 사진</div>
          <div>- 홍보 대상 사진</div>
        </div>
      </div>
      <div className="funding-regist-title">
        <label htmlFor="title">펀딩 제목</label>
        <input
          type="text"
          id="title"
          value={funding.title}
          onChange={onChange}
          placeholder="펀딩 제목을 입력해주세요"
        />
      </div>
      <div className="funding-regist-end-date">
        <label htmlFor="end-date">펀딩 마감일</label>
        <input
          type="date"
          id="endDate"
          value={funding.endDate}
          onChange={onChange}
          placeholder="펀딩 마감일을 선택해주세요"
        />
      </div>
      <div className="funding-regist-goal">
        <label htmlFor="goal">펀딩 목표액</label>
        <input
          type="number"
          id="goal"
          value={funding.goal}
          onChange={onChange}
          placeholder="펀딩 목표 금액을 입력해주세요"
        />
      </div>
      <div className="funding-regist-artist"></div>
      <WideButton onClick={onNext}>다음</WideButton>
    </form>
  );
}

FundingRegistInfo.propTypes = {
  funding: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  onNext: PropTypes.func.isRequired,
};

export default FundingRegistInfo;
