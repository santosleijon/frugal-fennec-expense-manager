import { Button, Card, CardContent, FormControl, TextField } from "@material-ui/core";
import { addAccount } from "modules/accounts/commands/addAccount";
import { dispatchCommand } from "modules/common/commands/dispatchCommand";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AccountsDataGrid } from "./AccountsDataGrid";
import { getAccounts } from "modules/accounts/commands/getAccounts";
import { Account } from "modules/accounts/types/Account";
import { AppState } from "modules/common/reducers/appReducer";

export default function Accounts() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatchCommand(getAccounts, dispatch)
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

    dispatchCommand(async () => {
      const result = await addAccount(account)
      setAccount({ ...account, name: "" })
      return result
    }, dispatch)
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
