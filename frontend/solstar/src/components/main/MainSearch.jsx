import Search from '../../assets/main/ColorSearch.png';
import BackButton from '../common/BackButton';
import './MainSearch.css';

function MainSearch() {
  const [searchValue, setSearchValue] = useState();

  return (
    <div className="main-search-container">
      <div className="main-search-bar">
        <BackButton />
        <input
          type="text"
          className="main-search-input"
          placeholder="펀딩, 아티스트 검색"
          value={searchValue}
          // onKeyDown={() = {}}
        />
      </div>
      <div className="main-search-description">
        <img src={Search} alt="" className="main-search-description-img" />
        <div>
          <div className="main-search-description-title">
            원하는 아티스트, 펀딩 검색
          </div>
          <div className="main-search-description-content">
            예) 아티스트 이름, 지하철 광고
          </div>
        </div>
      </div>
    </div>
  );
}

export default MainSearch;
