import { AppBar, Typography, Toolbar, Link, Box, Button, CircularProgress } from "@material-ui/core";
import { useNavigate } from 'react-router-dom';
import React from "react";
import './MenuBar.css'
import { useDispatch, useSelector } from "react-redux";
import { AppState } from "modules/common/reducers/appReducer";
import { User } from "modules/users/types/User";
import { dispatchCommand } from "modules/common/commands/dispatchCommand";
import { logout } from "modules/users/commands/logout"
import { deleteUser } from "modules/users/commands/deleteUser"
import DeleteUserConfirmDialog from "./DeleteUserConfirmDialog";

export default function MenuBar() {
  const navigate = useNavigate()
  const dispatch = useDispatch()

  const isLoadingCommand = useSelector<AppState, boolean>(
    (state) => state.isLoadingCommand
  )

  const loggedInUser = useSelector<AppState, User | null>(
    (state) => state.loggedInUser
  )

  const [isDeleteUserConfirmDialogOpen, setIsDeleteUserConfirmDialogOpen] = React.useState(false)

  const toolbarIcon = isLoadingCommand ? <CircularProgress color="inherit" size="32px" /> : <img src={process.env.PUBLIC_URL + '/frugal-fennec-icon-32x32.png'} alt="Frugal Fennec icon" />

  const onLogout = () => {
    dispatchCommand(logout, dispatch)
  }

  const onDeleteUser = () => {
    setIsDeleteUserConfirmDialogOpen(true)
  }

  const onConfirmDeleteUser = async () => {
    await dispatchCommand(deleteUser, dispatch)
    setIsDeleteUserConfirmDialogOpen(false)
  }

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar className="toolbar">
          <Box className="toolbar-icon">
            {toolbarIcon}
          </Box>
          <Typography variant="h6" className="toolbar-title" color="inherit">
            <Link href="./" title="Frugal Fennec Expense Manager" color="inherit">
              Frugal Fennec Expense Manager
            </Link>
          </Typography>
          { loggedInUser ? (
            <>
              Logged in as&nbsp;<strong>{loggedInUser.email}</strong>&nbsp;
              &middot;
              <Button color="inherit" onClick={onLogout}>Logout</Button>
              &middot;
              <Button color="inherit" onClick={() => onDeleteUser()}>Delete user</Button>
              &middot;
              <Button color="inherit" onClick={() => navigate('./', { replace: true })}>Reports</Button>
              &middot;
              <Button color="inherit" onClick={() => navigate('./expenses', { replace: true })}>Expenses</Button>
              &middot;
              <Button color="inherit" onClick={() => navigate('./accounts', { replace: true })}>Accounts</Button>

              <DeleteUserConfirmDialog
                isOpen={isDeleteUserConfirmDialogOpen}
                onConfirmDeleteUser={onConfirmDeleteUser}
                onClose={() => setIsDeleteUserConfirmDialogOpen(false)}
                userEmail={loggedInUser.email} />
            </>
          ) :
            <Button color="inherit" onClick={() => navigate('./', { replace: true })}>Login</Button>
          }
        </Toolbar>
      </AppBar>
    </Box>
  )
}
