import { Card, CardContent} from "@material-ui/core"
import { useState } from "react"
import { startLogin } from "modules/users/commands/startLogin"
import { dispatchCommand } from "modules/common/commands/dispatchCommand"
import { useDispatch } from "react-redux"
import { completeLogin } from "modules/users/commands/completeLogin"
import { useNavigate } from "react-router-dom"
import { CompleteLoginForm } from "./CompleteLoginForm"
import { StartLoginForm } from "./StartLoginForm"
import { abortLogin } from "modules/users/commands/abortLogin"

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

    const startLoginCommand = async () => { 
      const result = await startLogin(formValues.email)
      setLoginStarted(true)
      return result
    }

    dispatchCommand(startLoginCommand, dispatch)
  }

  const onVerificationCodeChanged = (event: React.ChangeEvent<HTMLInputElement>) => {
    const verificationCode = event.target.value
    
    if (verificationCode) {
      setFormValues({ ...formValues, verificationCode: verificationCode })
    }
  }

  const onSubmitCompleteLogin = (event: React.FormEvent) => {
    event.preventDefault()

    const completeLoginCommand = async () => {
      const result = await completeLogin(formValues.email, formValues.verificationCode)
      navigate("./")
      return result
    }

    dispatchCommand(completeLoginCommand, dispatch)
  }

  const onAbortLogin = () => {
    const abortLoginCommand = async () => {
      const result = await abortLogin(formValues.email)
      setFormValues({ ...formValues, ...{ email: "", verificationCode: "" } })
      setLoginStarted(false)
      return result
    }

    dispatchCommand(abortLoginCommand, dispatch)
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
                onAbort={onAbortLogin}
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
