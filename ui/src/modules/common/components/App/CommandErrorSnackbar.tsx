import { Alert, Snackbar } from "@mui/material";
import { useState } from "react";
import { useSelector } from "react-redux";
import { AppState } from "../../reducers/appReducer";

export default function CommandErrorSnackbar() {
    const [isOpen, setIsOpen] = useState(true)

    const errorMessage = useSelector<AppState, string | null>(
        (state) => state.commandErrorMessage
    )

    if (!errorMessage) {
        return <></>
    }

    function handleClose() {
        setIsOpen(false)
    }

    return (
        <Snackbar anchorOrigin={{ vertical: "top", horizontal: "center" }} open={isOpen} onClose={handleClose}>
            <Alert onClose={handleClose} severity="error">
                {errorMessage}
            </Alert>
        </Snackbar>
    )
}