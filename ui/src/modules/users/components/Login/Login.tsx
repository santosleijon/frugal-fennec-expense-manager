import { Avatar, Card, CardContent, List, ListItem, ListItemAvatar, ListItemText } from "@material-ui/core"
import { useCallback, useState } from "react"
import { startLogin } from "modules/users/commands/startLogin"
import { dispatchCommand } from "modules/common/commands/dispatchCommand"
import { useDispatch } from "react-redux"
import { completeLogin } from "modules/users/commands/completeLogin"
import { useNavigate } from "react-router-dom"
import { CompleteLoginForm } from "./CompleteLoginForm"
import { StartLoginForm } from "./StartLoginForm"
import { abortLogin } from "modules/users/commands/abortLogin"
import { AttachMoney, Lock, Timeline } from "@material-ui/icons";

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

    if (email != null) {
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
    
    if (verificationCode != null) {
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
      <h1>Take control of your expenses with Frugal Fennec</h1>
      <Card>
        <List>
            <ListItem>
              <ListItemAvatar>
                <Avatar>
                  <AttachMoney />
                </Avatar>
              </ListItemAvatar>
              <ListItemText
                primary="Record your expenses in a customizable accounts structure"
              />
            </ListItem>
            <ListItem>
              <ListItemAvatar>
                <Avatar>
                  <Timeline />
                </Avatar>
              </ListItemAvatar>
              <ListItemText
                primary="View expense reports to track your expenses over time"
              />
            </ListItem>
            <ListItem>
              <ListItemAvatar>
                <Avatar>
                  <Lock />
                </Avatar>
              </ListItemAvatar>
              <ListItemText
                primary="Login securely without a password using email verification"
              />
            </ListItem>
        </List>

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
