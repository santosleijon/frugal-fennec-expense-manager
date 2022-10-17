import { Alert, Snackbar } from "@mui/material";
import { resetCommandAction } from "modules/common/commands/resetCommandAction";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppState } from "../../reducers/appReducer";

export default function CommandErrorSnackbar() {
    const dispatch = useDispatch()

    const [isOpen, setIsOpen] = useState(true)

    const errorMessage = useSelector<AppState, string | null>(
        (state) => state.commandErrorMessage
    )

    useEffect(() => {
        setIsOpen(true)
    }, [errorMessage])

    if (!errorMessage) {
        return <></>
    }

    function handleClose() {
        dispatch(resetCommandAction)
    }

    return (
        <Snackbar anchorOrigin={{ vertical: "top", horizontal: "center" }} open={isOpen} onClose={(handleClose)}>
            <Alert onClose={handleClose} severity="error">
                {errorMessage}
            </Alert>
        </Snackbar>
    )
}