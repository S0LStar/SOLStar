import PropTypes from 'prop-types';
import WideButton from '../../common/WideButton';
import './FundingRegistInfo.css';
import ArtistSearchModal from './ArtistSearchModal';
import { useEffect, useState } from 'react';

import SelectIcon from '../../../assets/common/SelectIcon.png';

function FundingRegistInfo({ funding, onChange, onNext }) {
  const [nextActive, setNextActive] = useState(false); // 다음 버튼 활성화 상태
  const [artistSearchModal, setArtistSearchModal] = useState(false); // 아티스트 검색 모달 상태
  const [artistName, setArtistName] = useState('');

  // 모든 필드가 채워졌는지 확인
  useEffect(() => {
    const isFormComplete = Boolean(
      funding.type &&
        funding.title &&
        funding.deadlineDate &&
        funding.goalAmount &&
        funding.artistId
    );

    setNextActive(isFormComplete);
  }, [funding]);

  // 아티스트 검색 핸들링 함수
  const handleSelectArtist = (artist) => {
    // 아티스트의 Id값을 설정
    onChange({ target: { id: 'artistId', value: artist.artistId } });
    setArtistName(artist.name);
  };

  return (
    <form className="funding-regist-section">
      <div className="funding-regist-type">
        <label htmlFor="type">펀딩 종류</label>
        <div>
          <select
            type="select"
            id="type"
            value={funding.type || ''}
            onChange={onChange}
          >
            <option value="COMMON">일반 펀딩</option>
            <option value="VERIFIED">인증 펀딩</option>
            <option value="ADVERTISE">홍보글</option>
          </select>
          <img src={SelectIcon} alt="" className="funding-type-select-icon" />
        </div>
      </div>

      <div className="funding-regist-title">
        <label htmlFor="title">펀딩 제목</label>
        <input
          type="text"
          id="title"
          value={funding.title || ''}
          onChange={onChange}
          placeholder="펀딩 제목을 입력해주세요"
        />
      </div>
      <div className="funding-regist-end-date">
        <label htmlFor="deadlineDate">펀딩 마감일</label>
        <input
          type="text"
          id="deadlineDate"
          value={funding.deadlineDate || ''}
          onFocus={(e) => {
            e.target.type = 'date';
          }}
          onChange={onChange}
          placeholder="펀딩 마감일을 선택해주세요"
        />
      </div>
      <div className="funding-regist-goal">
        <label htmlFor="goalAmount">펀딩 목표액</label>
        <input
          type="number"
          id="goalAmount"
          value={funding.goalAmount || ''}
          onChange={onChange}
          placeholder="펀딩 목표 금액을 입력해주세요"
        />
      </div>
      <div className="funding-regist-artist">
        <label htmlFor="artist">대상 아티스트 선택</label>
        <input
          type="text"
          id="artist"
          value={artistName || ''}
          onClick={() => {
            setArtistSearchModal(true);
          }}
          placeholder="대상 아티스트를 선택해주세요"
          readOnly
        />
      </div>
      <WideButton onClick={onNext} isActive={nextActive}>
        다음
      </WideButton>

      <ArtistSearchModal
        isOpen={artistSearchModal}
        onClose={() => setArtistSearchModal(false)}
        onSelectArtist={handleSelectArtist}
      />
    </form>
  );
}

FundingRegistInfo.propTypes = {
  funding: PropTypes.object.isRequired,
  onChange: PropTypes.func.isRequired,
  onNext: PropTypes.func.isRequired,
};

export default FundingRegistInfo;
