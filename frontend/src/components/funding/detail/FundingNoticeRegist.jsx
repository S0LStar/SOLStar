import { useEffect, useState } from 'react';

import BackButton from '../../common/BackButton';
import WideButton from '../../common/WideButton';
import './FundingNoticeRegist.css';

import { useParams } from 'react-router-dom';
import axiosInstance from '../../../util/AxiosInstance';

function FundingNoticeRegist() {
  const { fundingId } = useParams();

  const [notice, setNotice] = useState({
    title: '',
    content: '',
  });
  const [registActive, setRegistActive] = useState(false);

  useEffect(() => {
    console.log(parseInt(fundingId));
    const isFormComplete = Boolean(notice.title && notice.content);

    setRegistActive(isFormComplete);

    console.log(notice);
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    setNotice({ ...notice, [id]: value });
  };

  // 공지 작성 함수
  const handleRegist = async () => {
    try {
      // TODO: 공지 작성 API 연결
      const response = await axiosInstance.post(
        `/funding/notice/${fundingId}`,
        { content: notice.content, title: notice.title }
      );

      console.log(response);
    } catch (error) {
      console.error('공지 작성 오류:', error);
      alert('공지글 등록 중 오류가 발생했습니다.');
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
    </div>
  );
}

export default FundingNoticeRegist;
