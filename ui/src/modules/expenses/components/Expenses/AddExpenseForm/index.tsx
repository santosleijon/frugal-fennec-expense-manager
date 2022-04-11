import { Button, Card, CardContent, FormControl, MenuItem, TextField } from "@material-ui/core"
import { useEffect, useState } from "react"
import './index.css'
import { getCurrentDate } from "utils/getCurrentDate"
import { Account } from "modules/accounts/types/Account"
import { Expense } from "modules/expenses/types/Expense"

interface AddExpenseFormProps {
  accounts: Account[],
  onExpenseAdded: (accountId: string, newExpense: Expense) => void
}

export default function AddExpenseForm(props: AddExpenseFormProps) {
  const initialFormValues = {
    date: getCurrentDate(),
    accountId: "",
    description: "",
    amount: "0.00",
  }

  const initialErrors = {
    description: "",
    amount: "",
  }

  const [values, setValues] = useState(initialFormValues)
  const [errors, setErrors] = useState(initialErrors)

  useEffect(() => {
    if (values.accountId === "" && props.accounts.length > 0) {
      setValues({ ...values, accountId: props.accounts[0]?.id ?? ""})
    }
  }, [values, props.accounts, setValues])

  const onDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const date = event.target.value

    if (date) {
      setValues({ ...values, date: date })
    }
  }

  const onAccountChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValues({ ...values, accountId: event.target.value })
  }
  const onAmountChanged = (event: any) => {
    setValues({ ...values, amount: event.target.value})
  }

  const onAmountBlur = (_: any) => {
    const integerRegex = /^-?\d{1,64}$/g
    const amountRegex = /^-?\d{1,64}.\d{2}$/g
    
    if (values.amount.match(integerRegex)) {
      setValues({ ...values, amount: values.amount + ".00"})
      return
    }

    if (!values.amount.match(amountRegex)) {
      setErrors({ ...errors, amount: "Amount must be specified to two decimal places" })
    } else {
      setErrors({ ...errors, amount: "" })
    }
  }
  
  const onDescriptionChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const descriptionRegex = /^.{0,255}$/g

    if (!event.target.value.match(descriptionRegex)) {
      setErrors({ ...errors, description: "Description must be no more than 255 characters" })
    } else {
      setErrors({ ...errors, description: "" })
      setValues({ ...values, description: event.target.value})
    }
  }

  const onSubmit = (event: React.FormEvent) => {
    event.preventDefault()

    const noValidationErrors = Object.values(errors).every((error) => error === "")

    if (noValidationErrors) {
      const newExpense: Expense = {
        date: values.date,
        description: values.description,
        amount: +values.amount,
      }

      if (values.accountId) {
        props.onExpenseAdded(values.accountId, newExpense)

        setValues({
          ...values,
          description: "",
          amount: "0.00"
        })
      }
    }
  }

  if (props.accounts.length < 1) {
    return <></>
  }

  return (
    <Card>
      <CardContent>
        <form noValidate autoComplete="off" onSubmit={onSubmit}>
          <h3>Add expense</h3>
          
          <FormControl variant="outlined">
            <div className="addExpenseForm">
              <TextField
                  id="date-field"
                  label="Date"
                  type="date"
                  value={values.date}
                  InputLabelProps={{
                    shrink: true,
                  }}
                  variant="outlined"
                  onChange={onDateChange}
                  className="inputField"
              />

              {/* TODO: Use common AccountFormField component? */}
              <TextField
                  id="account-field"
                  select
                  label="Account"
                  value={values.accountId ?? props.accounts[0]?.id}
                  variant="outlined"
                  onChange={onAccountChange}
                  className="inputField"
              >
                {props.accounts.map((account) => (
                <MenuItem key={account.id} value={account.id}>
                    {account.name}
                </MenuItem>
                ))}
              </TextField>
              
              <TextField
                  id="description-field"
                  label="Description"
                  value={values.description}
                  className="inputField"
                  onChange={onDescriptionChange}
                  error={errors.description !== ""}
                  variant="outlined"
                  helperText={errors.description}
              />
              
              <TextField
                  id="amount-field"
                  label="Amount"
                  value={values.amount}
                  onBlur={onAmountBlur}
                  onChange={onAmountChanged}
                  variant="outlined"
                  className="inputField"
                  error={errors.amount !== ""}
                  helperText={errors.amount}
              /> 

              <Button
                  variant="contained"
                  color="primary"
                  size="large"
                  className="submitButton"
                  type="submit"
              >
                  Add
              </Button>
            </div>
          </FormControl>
        </form>
      </CardContent>
    </Card>
  )
}