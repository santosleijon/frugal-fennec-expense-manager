import { AppBar, Typography, Toolbar, Link, Box, Button, CircularProgress } from "@material-ui/core";
import AccountBalanceIcon from '@material-ui/icons/AccountBalance';
import { useNavigate } from 'react-router-dom';
import React from "react";
import './index.css'
import { useSelector } from "react-redux";
import { AppState } from "modules/common/reducers/appReducer";

export default function MenuBar() {
  const navigate = useNavigate();

  const isLoadingCommand = useSelector<AppState, boolean>(
    (state) => state.isLoadingCommand
  );

  const toolbarIcon = isLoadingCommand ? <CircularProgress color="inherit" size="24px" /> : <AccountBalanceIcon />

  return (
    <Box sx={{ flexGrow: 1 }}>
    <AppBar position="static">
      <Toolbar className="toolbar">
        <Box className="toolbar-icon">
          {toolbarIcon}
        </Box>
        <Typography variant="h6" className="toolbar-title" color="inherit">
          <Link href="./" title="Frugal Fennec Expense Manager" color="inherit">
            Frugal Fennec
          </Link>
        </Typography>
        <Button color="inherit" onClick={() => navigate('./', { replace: true })}>Reports</Button>
        <Button color="inherit" onClick={() => navigate('./expenses', { replace: true })}>Expenses</Button>
        <Button color="inherit" onClick={() => navigate('./accounts', { replace: true })}>Accounts</Button>
      </Toolbar>
    </AppBar>
    </Box>
  )
}
