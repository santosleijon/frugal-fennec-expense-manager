import { DataGrid, GridColDef } from '@material-ui/data-grid';
import React, { useState } from 'react';
import AppToolbar from './AppToolbar';
import Footer from './Footer';
import Main from './Main';

function App() {
  return (
    <>
      <AppToolbar />
      <Main />
      <Footer />
    </>
  )
}

export default App;
