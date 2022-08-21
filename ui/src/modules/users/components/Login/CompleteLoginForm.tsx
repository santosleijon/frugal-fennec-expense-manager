import { Button, FormControl, TextField } from "@material-ui/core"

interface CompleteLoginFormProps {
  email: string,
  verificationCode: string,
  onVerificationCodeChanged: (event: React.ChangeEvent<HTMLInputElement>) => void,
  onSubmit: (event: React.FormEvent) => void
  onAbort: () => void
}

export function CompleteLoginForm(props: CompleteLoginFormProps) {
  return (
    <>
      <form noValidate onSubmit={props.onSubmit}>
        <h3>Complete login by entering verification code sent by email</h3>
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
            onChange={props.onVerificationCodeChanged}
            style={{ flexGrow: 1, marginRight: "12px" }}
          />

          <Button
            variant="contained"
            color="primary"
            size="large"
            className="submitButton"
            type="submit"
            style={{ marginRight: "12px" }}
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