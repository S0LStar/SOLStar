import { createRoot } from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { BrowserRouter } from 'react-router-dom';
import { Provider } from 'react-redux'; // Redux Provider import
import store from './redux/Redux'; // Redux store import

createRoot(document.getElementById('root')).render(
  <Provider store={store}>
    {' '}
    {/* Redux Provider로 감싸기 */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </Provider>
);
