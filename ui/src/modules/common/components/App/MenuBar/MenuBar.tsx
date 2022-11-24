import { AppBar, Toolbar, Link, Box, CircularProgress, Divider } from "@material-ui/core";
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
import { Drawer, Button, IconButton, List, ListItem, ListItemButton, ListItemText, Typography } from "@mui/material";
import { Menu } from "@material-ui/icons";

export default function MenuBar() {
  const navigate = useNavigate()
  const dispatch = useDispatch()

  const isLoadingCommand = useSelector<AppState, boolean>(
    (state) => state.isLoadingCommand
  )

  const loggedInUser = useSelector<AppState, User | null>(
    (state) => state.loggedInUser
  )

  const [mobileOpen, setMobileOpen] = React.useState(false)

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

  const drawerWidth = 240

  const navItems = loggedInUser ? [
    { key: 'expenses', text: 'Expenses', onClick: () => navigate('./', { replace: true }) },
    { key: 'accounts', text: 'Accounts', onClick: () => navigate('./accounts', { replace: true }) },
    { key: 'reports', text: 'Reports', onClick: () => navigate('./reports', { replace: true }) },
    { key: 'delete-user', text: 'Delete user', onClick: onDeleteUser },
    { key: 'logout', text: 'Logout', onClick: onLogout },
  ] : [
    { key: 'login', text: 'Login', onClick: () => navigate('./', { replace: true }) },
  ]

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  }

  const drawer = (
    <Box onClick={handleDrawerToggle} sx={{ textAlign: 'center' }}>
      <Typography variant="h6" className="toolbar-title" color="inherit">
        <Link href="./" title="Frugal Fennec Expense Manager" color="inherit">
          Frugal Fennec Expense Manager
        </Link>
      </Typography>
      <Divider />
      <List>
        {navItems.map((item) => (
          <ListItem key={item.key} disablePadding>
            <ListItemButton onClick={item.onClick} sx={{ textAlign: 'center' }}>
              <ListItemText primary={item.text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  )

  return (
    <Box sx={{ display: 'flex' }}>
      <AppBar component="nav" position="static">
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={handleDrawerToggle}
            sx={{ mr: 2, display: { sm: 'none' } }}
          >
            <Menu />
          </IconButton>
          <Box className="toolbar-icon">
            {toolbarIcon}
          </Box>
          <Typography variant="h6" className="toolbar-title" color="inherit">
            <Link href="./" title="Frugal Fennec Expense Manager" color="inherit">
              Frugal Fennec Expense Manager
            </Link>
          </Typography>
          <Box sx={{ display: { xs: 'none', sm: 'block' } }}>
            {navItems.map((item) => (
              <Button key={item.key} onClick={item.onClick} sx={{ color: '#fff' }}>
                {item.text}
              </Button>
            ))}
          </Box>
        </Toolbar>
      </AppBar>
      <Box component="nav">
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{
            keepMounted: true, // Better open performance on mobile.
          }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
          }}
        >
          {drawer}
        </Drawer>
      </Box>
      {loggedInUser &&
        <DeleteUserConfirmDialog
          isOpen={isDeleteUserConfirmDialogOpen}
          onConfirmDeleteUser={onConfirmDeleteUser}
          onClose={() => setIsDeleteUserConfirmDialogOpen(false)}
          userEmail={loggedInUser.email} />
      }
    </Box>
  )
}
