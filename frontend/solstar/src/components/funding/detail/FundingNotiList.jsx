import './FundingNotiList.css';
import PropTypes from 'prop-types';

function FundingNotiList({ isHost }) {
  const tempData = [
    {
      id: 2,
      nickname: '뉴진스 성덕',
      title: '두번째 공지입니다',
      content: '곧 마감될 것 같습니다',
      contentImage: null,
      createDate: '2024-08-29T00:51:12.474547',
      profileImage: 'https://example.com/profile1.png',
    },
    {
      id: 1,
      nickname: '뉴진스 성덕',
      title: '공지입니다',
      content: '벌써 돈이 모였습니다!!!!',
      contentImage: null,
      createDate: '2024-08-28T00:50:19.938904',
      profileImage: 'https://example.com/profile2.png',
    },
  ];

  function formatTimeDifference(date) {
    const now = new Date();
    const noticeDate = new Date(date);
    const timeDiff = now - noticeDate;
    const hours = Math.floor(timeDiff / (1000 * 60 * 60));
    const days = Math.floor(hours / 24);

    if (hours < 24) {
      return `${hours}시간 전`;
    } else {
      return `${days}일 전`;
    }
  }

  return (
    <div>
      {isHost && <button>공지글 작성하기</button>}
      {tempData.map((notice) => (
        <div key={notice.id} className="notice-item">
          <div className="notice-profile-info">
            <img
              src={notice.profileImage}
              alt="프로필 이미지"
              className="profile-image"
            />
            <div className="notice-user-info">
              <div>{notice.nickname}</div>
              <p className="time-difference">
                {formatTimeDifference(notice.createDate)}
              </p>
            </div>
          </div>
          <h3>{notice.title}</h3>
          <div className="notice-content">{notice.content}</div>
        </div>
      ))}
    </div>
  );
}

FundingNotiList.propTypes = {
  isHost: PropTypes.bool.isRequired,
};

export default FundingNotiList;
