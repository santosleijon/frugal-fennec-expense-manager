import { appReducer } from 'reducers/appReducer';
import AppToolbar from './AppToolbar';
import Footer from './Footer';
import Main from './Main';
import { createStore } from 'redux';
import { Provider } from 'react-redux';
import { devToolsEnhancer  } from 'redux-devtools-extension';
import CommandErrorSnackbar from './CommandErrorSnackbar';

function App() {
  let store = createStore(appReducer, devToolsEnhancer({}));

  return (
    <Provider store={store}>
      <AppToolbar />
      <Main />
      <Footer />
      <CommandErrorSnackbar />
    </Provider>
  )
}

export default App;
