import React, { useEffect, useState } from 'react';

import BackButton from '../../common/BackButton';
import WideButton from '../../common/WideButton';
import './FundingNoticeRegist.css';
import Error from '../../common/Error';

import DefaultImage from '../../../assets/funding/DefaultImage.png';
import { useParams } from 'react-router-dom';
import axiosInstance from '../../../util/AxiosInstance';

function FundingNoticeRegist() {
  const { fundingId } = useParams();
  const [error, setError] = useState(false);

  const [notice, setNotice] = useState({
    title: '',
    contentImage: null,
    content: '',
  });
  const [previewImage, setPreviewImage] = useState(null); // 이미지 미리보기 상태
  const [registActive, setRegistActive] = useState(false);

  useEffect(() => {
    console.log(parseInt(fundingId));
    const isFormComplete = Boolean(notice.title && notice.content);

    setRegistActive(isFormComplete);

    console.log(notice);
  });

  // 참조를 통해 파일 입력 요소에 접근
  const fileInputRef = React.createRef();

  const handleChange = (e) => {
    const { id, value } = e.target;
    setNotice({ ...notice, [id]: value });
  };

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
      handleChange({ target: { id: 'contentImage', value: file } });
    }
  };

  // 공지 작성 함수
  const handleRegist = async () => {
    try {
      const noticeData = new FormData();
      noticeData.append(
        'notice',
        new Blob(
          [JSON.stringify({ title: notice.title, content: notice.content })],
          {
            type: 'application/json',
          }
        )
      );

      if (notice.contentImage) {
        const fileBlob = new Blob([notice.contentImage], { type: 'image/png' });
        const fileData = new File([fileBlob], 'image.png');
        noticeData.append('contentImage', fileData);
      }

      console.log(noticeData);
      // 공지 작성 API 연결
      const response = await axiosInstance.post(
        `/funding/notice/${fundingId}`,
        noticeData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }
      );

      console.log(response);
    } catch (error) {
      console.error('공지 작성 오류:', error);
      setError(error);
    }
  };

  return (
    <div className="funding-notice-regist-container">
      <header>
        <BackButton />
        <div className="funding-notice-header-title">어떤 공지글 인가요 ?</div>
      </header>
      <div className="funding-notice-form-container">
        <form>
          <div className="funding-regist-title">
            <label htmlFor="title">공지 제목</label>
            <input
              type="text"
              id="title"
              value={notice.title || ''}
              onChange={handleChange}
              placeholder="펀딩 제목을 입력해주세요"
            />
          </div>

          <div className="funding-regist-image">
            <label htmlFor="contentImage">공지 대표 사진</label>
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
                  {/* TODO: notice.contentImage ||  */}
                  <img
                    src={DefaultImage}
                    alt=""
                    className="funding-regist-default-image"
                  />
                </div>
              )}
              <input
                type="file"
                id="contentImage"
                ref={fileInputRef}
                accept="image/*"
                onChange={handleImageChange}
                style={{ display: 'none' }}
              />
              <div className="funding-regist-image-description">
                <div>이런 사진을 등록해보세요 !</div>
                <div>- 공지 내용을 요약한 사진</div>
                <div>- 정산 후 내역 사진</div>
              </div>
            </div>
          </div>

          <div className="funding-regist-title">
            <label htmlFor="content">공지 내용</label>
            <input
              type="text"
              id="content"
              value={notice.content || ''}
              onChange={handleChange}
              placeholder="펀딩 제목을 입력해주세요"
            />
          </div>
        </form>
      </div>
      <WideButton isActive={registActive} onClick={handleRegist}>
        확인
      </WideButton>

      {error && <Error setError={setError} />}
    </div>
  );
}

export default FundingNoticeRegist;
