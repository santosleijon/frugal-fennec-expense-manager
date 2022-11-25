import { FormControl, TextField } from "@material-ui/core"
import { Button } from "@mui/material"
import { useEffect, useState } from "react"

interface CompleteLoginFormProps {
  email: string,
  verificationCode: string,
  onVerificationCodeChanged: (event: React.ChangeEvent<HTMLInputElement>) => void,
  onSubmit: (event: React.FormEvent) => void
  onAbort: () => void
}

export function CompleteLoginForm(props: CompleteLoginFormProps) {

  const [secondsUntilVerificationCodeExpires, setSecondsUntilVerificationCodeExpires] = useState(60)

  const verificationCodeHasExpired = secondsUntilVerificationCodeExpires < 1

  useEffect(() => {
    const timer = setInterval(() => {
      if (secondsUntilVerificationCodeExpires > 0) {
        setSecondsUntilVerificationCodeExpires(secondsUntilVerificationCodeExpires-1)
      }
    }, 1000);

    return () => clearInterval(timer);
  }, [secondsUntilVerificationCodeExpires, setSecondsUntilVerificationCodeExpires]);

  return (
    <>
      <form noValidate onSubmit={props.onSubmit}>
        <h3>Complete login by entering verification code sent by email (expires in {secondsUntilVerificationCodeExpires} seconds)</h3>
        <FormControl variant="outlined" style={{ display: "flex", alignItems: "flex-start", flexDirection: "row" }}>
          <TextField
            id="email-field"
            label="Email"
            value={props.email}
            disabled={true}
            variant="outlined"
            style={{ flexGrow: 1, marginRight: "12px" }}
          />

          <TextField
            id="verification-code-field"
            label="Verification code"
            value={props.verificationCode}
            InputLabelProps={{
              shrink: true,
            }}
            variant="outlined"
            autoFocus={true}
            autoComplete="off"
            onChange={props.onVerificationCodeChanged}
            disabled={verificationCodeHasExpired}
            style={{ flexGrow: 1, marginRight: "12px" }}
          />

          <Button
            variant="contained"
            color="primary"
            size="large"
            className="submitButton"
            type="submit"
            style={{ marginRight: "12px" }}
            disabled={verificationCodeHasExpired}
          >
            Complete login
          </Button>

          <Button
            variant="contained"
            color="secondary"
            size="small"
            className="abortButton"
            type="button"
            style={{ alignSelf: "center" }}
            onClick={() => props.onAbort()}
          >
            Abort
          </Button>
        </FormControl>
      </form>
    </>
  )
}