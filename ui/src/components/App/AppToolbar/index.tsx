import { AppBar, Typography, Toolbar, Link, Box, Button } from "@material-ui/core";
import AccountBalanceIcon from '@material-ui/icons/AccountBalance';
import { useNavigate } from 'react-router-dom';
import React from "react";
import './index.css'

export default function AppToolbar() {
  const navigate = useNavigate();

  return (
    <Box sx={{ flexGrow: 1 }}>
    <AppBar position="static">
      <Toolbar className="toolbar">
        <AccountBalanceIcon className="toolbar-icon" />
        <Typography variant="h6" className="toolbar-title" color="inherit">
          <Link href="./" title="Frugal Fennec Expense Manager" color="inherit">
            Frugal Fennec
          </Link>
        </Typography>
        <Button color="inherit" onClick={() => navigate('./', { replace: true })}>Expenses</Button>
        <Button color="inherit" onClick={() => navigate('./accounts', { replace: true })}>Accounts</Button>
      </Toolbar>
    </AppBar>
    </Box>
  )
}
