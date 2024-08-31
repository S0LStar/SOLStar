import './FundingNotiList.css';
import PropTypes from 'prop-types';

import Notice from '../../../assets/funding/Notice.png';
import More from '../../../assets/funding/More.png';
import DefaultImage from '../../../assets/common/DefaultArtist.png';

import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axiosInstance from '../../../util/AxiosInstance';
import Modal from '../../common/Modal';

function FundingNotiList({ fundingId, isHost, nickname, profileImage }) {
  const navigate = useNavigate();
  const [selectedMore, setSelectedMore] = useState(null);
  const [fundingNotice, setFundingNotice] = useState([]);
  const [successDelete, setSuccessDelete] = useState(false);

  useEffect(() => {
    const fetchFundingNotice = async () => {
      try {
        const response = await axiosInstance.get(
          `/funding/notice/${fundingId}`
        );
        console.log(response);
        setFundingNotice(response.data.data.noticeList);
      } catch (error) {
        console.error('펀딩 공지 내용 조회 중 오류 발생', error);
      }
    };

    fetchFundingNotice();
  }, []);

  function formatTimeDifference(date) {
    const now = new Date();
    const noticeDate = new Date(date);
    const timeDiff = now - noticeDate;
    const hours = Math.floor(timeDiff / (1000 * 60 * 60));
    const days = Math.floor(hours / 24);

    if (hours < 1) {
      return `1시간 내`;
    } else if (hours < 24) {
      return `${hours}시간 전`;
    } else {
      return `${days}일 전`;
    }
  }

  const handleMoreClick = (noticeId) => {
    if (selectedMore === noticeId) {
      setSelectedMore(null);
    } else {
      setSelectedMore(noticeId); // 모달을 열고자 하는 공지의 ID를 설정
    }
  };

  const handleCloseModal = () => {
    setSelectedMore(null); // 모달 닫기
  };

  const handleDeleteNotice = async (boardId) => {
    console.log(boardId);
    const reseponse = await axiosInstance.delete(`/funding/notice/${boardId}`);
    handleCloseModal();
    console.log(reseponse);
    setSuccessDelete(true);
  };

  return (
    <div>
      {isHost && (
        <div
          className="regist-notice-button"
          onClick={() => {
            navigate(`notice`);
          }}
        >
          <div className="regist-notice-button-description">
            공지글 작성하기
          </div>
          <img src={Notice} alt="" />
        </div>
      )}
      {fundingNotice.map((notice) => (
        <div key={notice.boardId} className="notice-item">
          <div className="notice-profile-info">
            <img
              src={DefaultImage}
              alt="프로필 이미지"
              className="profile-image"
            />
            <div className="notice-user-info">
              <div>{nickname}</div>
              <p className="time-difference">
                {formatTimeDifference(notice.createDate)}
              </p>
            </div>
            {isHost && (
              <img
                src={More}
                alt=""
                className="more"
                onClick={() => handleMoreClick(notice.boardId)}
              />
            )}
          </div>
          <h3>{notice.title}</h3>
          <div className="notice-content">{notice.content}</div>
          {notice.contentImage && (
            <img
              src={notice.contentImage}
              className="notice-content-image"
            ></img>
          )}

          {selectedMore === notice.boardId && (
            <div className="more-modal">
              <div className="more-modal-content">
                <div
                  className="more-modal-item"
                  onClick={() => handleDeleteNotice(notice.boardId)}
                >
                  글 삭제하기
                </div>
              </div>
            </div>
          )}
        </div>
      ))}

      {successDelete && (
        <Modal
          mainMessage="공지를 삭제하였습니다."
          onClose={() => setSuccessDelete(false)}
        />
      )}
    </div>
  );
}

FundingNotiList.propTypes = {
  isHost: PropTypes.bool.isRequired,
  fundingId: PropTypes.number.isRequired,
  nickname: PropTypes.string.isRequired,
  profileImage: PropTypes.string.isRequired,
};

export default FundingNotiList;
