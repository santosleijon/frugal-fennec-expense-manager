import { Button, Card, CardContent, FormControl, TextField } from "@material-ui/core";
import { addAccount } from "commands/addAccount";
import { dispatchCommand } from "commands/dispatchCommand";
import { getAccounts } from "commands/getAccounts";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppState } from "reducers/appReducer";
import { Account } from "types/Account";
import { AccountsDataGrid } from "./AccountsDataGrid";

export default function Accounts() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatchCommand(getAccounts, dispatch)
    // TODO: Display error message with notistack
  }, [dispatch]);

  const accounts = useSelector<AppState, Account[]>(
    (state) => state.accounts
  );

  return (
    <>
      <h1>Accounts</h1>
      <AddAccountForm />
      <AccountsDataGrid accounts={accounts} />
    </>
  )
}

function AddAccountForm() {
  const dispatch = useDispatch();

  const [account, setAccount] = useState<Account>({ name: "", expenses: [] })

  const onSubmit = (event: React.FormEvent) => {
    event.preventDefault()

    dispatchCommand(() => addAccount(account), dispatch)

    setAccount({ ...account, name: "" })

    // TODO: Display error message with notistack
  }

  const onChangeAccountName = (event: React.ChangeEvent<HTMLInputElement>) => {
    setAccount({ ...account, name: event.target.value })
  }

  return (
    <>
      <Card>
        <CardContent>
          <form noValidate autoComplete="off" onSubmit={onSubmit}>
            <h3>Add account</h3>
            <FormControl variant="outlined" style={{display: "flex", alignItems: "flex-start", flexDirection: "row"}}>
              <TextField
                id="name-field"
                label="Name"
                value={account.name}
                onChange={onChangeAccountName}
                variant="outlined"
                style={{ flexGrow: 1, marginRight: "12px" }}
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
            </FormControl>
          </form>
        </CardContent>
      </Card>
    </>
  )
}
