import { Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle } from "@material-ui/core";
import { Button } from "@mui/material";

interface DeleteUserConfirmDialogProps {
  isOpen: boolean
  onConfirmDeleteUser: () => void
  onClose: () => void
  userEmail: string
}

export default function DeleteUserConfirmDialog(props: DeleteUserConfirmDialogProps) {
  return (
    <Dialog
      open={props.isOpen}
      onClose={props.onClose}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogTitle id="alert-dialog-title">
        {"Delete user "} <em>{props.userEmail}</em>?
      </DialogTitle>
      <DialogContent>
        <DialogContentText id="alert-dialog-description">
          Are you sure you want to permanently delete your user account and all its associated data?
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button onClick={props.onClose}>Abort</Button>
        <Button onClick={props.onConfirmDeleteUser}>Permanently delete user</Button>
      </DialogActions>
    </Dialog>
  )
}
