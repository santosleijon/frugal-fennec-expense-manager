import { Box, Card, CardContent } from "@material-ui/core";
import { Account } from "modules/accounts/types/Account";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { AppState } from "modules/common/reducers/appReducer";
import { SelectAccountFormField } from "../../common/components/App/SelectAccountFormField";
import { dispatchCommand } from "modules/common/commands/dispatchCommand";
import { getAccounts } from "modules/accounts/commands/getAccounts";
import { ExpenseReport } from "./ExpenseReport";
import { User } from "modules/users/types/User";

export default function Reports() {
  const dispatch = useDispatch();

  const userIsLoggedIn = useSelector<AppState, User | null>(
    (state) => state.loggedInUser
  ) != null;

  const accounts = useSelector<AppState, Account[]>(
    (state) => state.accounts
  );
 
  const [selectedAccount, setSelectedAccount] = useState<Account | undefined>(undefined)

  useEffect(() => {
    if (userIsLoggedIn) {
      dispatchCommand(getAccounts, dispatch)
    }
  }, [dispatch, userIsLoggedIn]);

  useEffect(() => {
    setSelectedAccount(accounts[0])
  }, [accounts])

  const onSelectAccount = (selectedAccountId: string) => {
    let selectedAccount = accounts.find((account) => account.id === selectedAccountId)

    setSelectedAccount(selectedAccount)
  }

  return (
    <>
      <h1>Reports</h1>
      <Card>
        <CardContent>
          <h2>Expenses by account</h2>

          <SelectAccountFormField
            accounts={accounts}
            selectedAccountId={selectedAccount?.id}
            onSelectAccount={onSelectAccount}
          />

          <Box sx={{ mt: 2 }}>
            {selectedAccount && (
              <ExpenseReport account={selectedAccount} />
            )}
          </Box>
        </CardContent>
      </Card>
    </>
  )
}
