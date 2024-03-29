import MenuBar from './MenuBar/MenuBar';
import Footer from './Footer/Footer';
import Main from './Main/Main';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import { devToolsEnhancer  } from 'redux-devtools-extension';
import CommandErrorSnackbar from './CommandErrorSnackbar';
import { appReducer } from '../../reducers/appReducer';

function App() {
  let store = createStore(appReducer, devToolsEnhancer({}));

  return (
    <Provider store={store}>
      <MenuBar />
      <Main />
      <Footer />
      <CommandErrorSnackbar />
    </Provider>
  )
}

export default App;
