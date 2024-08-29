import PropTypes from 'prop-types';
import WideButton from '../../common/WideButton';
import './FundingRegistInfo.css';
import ArtistSearchModal from './ArtistSearchModal';
import React, { useEffect, useState } from 'react';

import DefaultImage from '../../../assets/funding/DefaultImage.png';
import SelectIcon from '../../../assets/common/SelectIcon.png';

function FundingRegistInfo({ funding, onChange, onNext }) {
  const [nextActive, setNextActive] = useState(false); // 다음 버튼 활성화 상태
  const [artistSearchModal, setArtistSearchModal] = useState(false); // 아티스트 검색 모달 상태
  const [previewImage, setPreviewImage] = useState(null); // 이미지 미리보기 상태
  const [artistName, setArtistName] = useState('');

  // 모든 필드가 채워졌는지 확인
  useEffect(() => {
    const isFormComplete = Boolean(
      funding.type &&
        funding.title &&
        funding.deadlineDate &&
        funding.goalAmount &&
        funding.fundingImage &&
        funding.artistId
    );

    setNextActive(isFormComplete);
  }, [funding]);

  // 참조를 통해 파일 입력 요소에 접근
  const fileInputRef = React.createRef();

  // 이미지 업로드 함수
  const handleImageUploadClick = () => {
    // 숨겨진 파일 입력 필드를 클릭
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  // 이미지 변경 함수
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      // 미리보기 이미지 설정
      setPreviewImage(URL.createObjectURL(file));
      onChange({ target: { id: 'fundingImage', value: file } });
    }
  };

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

      <div className="funding-regist-image">
        <label htmlFor="image">펀딩 대표 사진</label>
        <div className="funding-regist-image-content">
          {previewImage ? (
            <img
              src={previewImage}
              alt="미리보기"
              className="funding-regist-image-preview"
              onClick={handleImageUploadClick}
            />
          ) : (
            <div
              className="funding-regist-image-box"
              onClick={handleImageUploadClick}
            >
              <img
                src={DefaultImage}
                alt=""
                className="funding-regist-default-image"
              />
            </div>
          )}
          <input
            type="file"
            id="image"
            ref={fileInputRef}
            accept="image/*"
            onChange={handleImageChange}
            style={{ display: 'none' }}
          />
          <div className="funding-regist-image-description">
            <div>이런 사진을 등록해보세요 !</div>
            <div>- 아티스트 사진</div>
            <div>- 홍보 대상 사진</div>
          </div>
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
