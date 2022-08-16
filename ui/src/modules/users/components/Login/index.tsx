import { Button, Card, CardContent, FormControl, TextField } from "@material-ui/core"
import { useState } from "react"
import { startLogin } from "modules/users/commands/startLogin"
import { dispatchCommand } from "modules/common/commands/dispatchCommand"
import { useDispatch } from "react-redux"
import { completeLogin } from "modules/users/commands/completeLogin"
import { useNavigate } from "react-router-dom"

export default function Login() {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const initialFormValues = {
    email: "",
    verificationCode: "",
  }

  const [formValues, setFormValues] = useState(initialFormValues)
  const [loginStarted, setLoginStarted] = useState(false)

  const onEmailChanged = (event: React.ChangeEvent<HTMLInputElement>) => {
    const email = event.target.value

    if (email) {
      setFormValues({ ...formValues, email: email })
    }
  }

  const onSubmitStartLogin = async (event: React.FormEvent) => {
    event.preventDefault()

    dispatchCommand(async () => { 
      const result = await startLogin(formValues.email)
      setLoginStarted(true)
      return result
    }, dispatch)
  }

  const onVerificationCodeChanged = (event: React.ChangeEvent<HTMLInputElement>) => {
    const verificationCode = event.target.value
    
    if (verificationCode) {
      setFormValues({ ...formValues, verificationCode: verificationCode })
    }
  }

  const onSubmitCompleteLogin = (event: React.FormEvent) => {
    event.preventDefault()

    const onSubmitCommand = async () => {
      const result = await completeLogin(formValues.email, formValues.verificationCode)
      navigate("../reports")
      return result
    }

    dispatchCommand(onSubmitCommand, dispatch)
  }

  return (
    <>
      <h1>Login</h1>
      <Card>
          <CardContent>
            {loginStarted ?
              <CompleteLoginForm
                email={formValues.email}
                verificationCode={formValues.verificationCode}
                onVerificationCodeChanged={onVerificationCodeChanged}
                onSubmit={onSubmitCompleteLogin}
              />
              :
              <StartLoginForm
                email={formValues.email}
                onEmailChanged={onEmailChanged}
                onSubmit={onSubmitStartLogin}
              />
            }
          </CardContent>
        </Card>
    </>
  )
}


interface StartLoginFormProps {
  email: string,
  onEmailChanged: (event: React.ChangeEvent<HTMLInputElement>) => void,
  onSubmit: (event: React.FormEvent) => void
}

function StartLoginForm(props: StartLoginFormProps) {
  return (
    <>
      <form noValidate onSubmit={props.onSubmit}>
        <h3>Enter email to start login</h3>
        <FormControl variant="outlined" style={{display: "flex", alignItems: "flex-start", flexDirection: "row"}}>
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
            Start login
          </Button>
        </FormControl>
      </form>
    </>
  )
}

interface CompleteLoginFormProps {
  email: string,
  verificationCode: string,
  onVerificationCodeChanged: (event: React.ChangeEvent<HTMLInputElement>) => void,
  onSubmit: (event: React.FormEvent) => void
}

// TODO: Add "reset login" button to abort a started login

function CompleteLoginForm(props: CompleteLoginFormProps) {
  return (
    <>
      <form noValidate onSubmit={props.onSubmit}>
        <h3>Complete login by entering verification code sent by email</h3>
        <FormControl variant="outlined" style={{display: "flex", alignItems: "flex-start", flexDirection: "row"}}>
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
          >
            Complete login
          </Button>
        </FormControl>
      </form>
    </>
  )
}