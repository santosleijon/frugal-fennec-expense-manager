import { AppBar, Typography, Toolbar } from "@material-ui/core";
import AccountBalanceIcon from '@material-ui/icons/AccountBalance';
import React from "react";
import './index.css'

export default function AppToolbar() {
  return (
    <AppBar position="static" elevation={0}>
      <Toolbar className="toolbar">
        <AccountBalanceIcon className="toolbar-icon" />
        <Typography variant="h6" color="inherit" className="toolbar-title">
          Finance Lynx
        </Typography>
      </Toolbar>
    </AppBar>
  )
}