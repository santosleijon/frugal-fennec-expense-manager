import { Button, FormControl, TextField } from "@material-ui/core"

interface StartLoginFormProps {
  email: string,
  onEmailChanged: (event: React.ChangeEvent<HTMLInputElement>) => void,
  onSubmit: (event: React.FormEvent) => void
}

export function StartLoginForm(props: StartLoginFormProps) {
  return (
    <>
      <form noValidate onSubmit={props.onSubmit}>
        <h3>Enter your email to login or register a new user</h3>
        <FormControl variant="outlined" style={{ display: "flex", alignItems: "flex-start", flexDirection: "row" }}>
          <TextField
            id="email-field"
            label="Email"
            value={props.email}
            InputLabelProps={{
              shrink: true,
            }}
            variant="outlined"
            onChange={props.onEmailChanged}
            style={{ flexGrow: 1, marginRight: "12px" }}
          />

          <Button
            variant="contained"
            color="primary"
            size="large"
            className="submitButton"
            type="submit"
          >
            Verify email
          </Button>
        </FormControl>
      </form>
    </>
  )
}
