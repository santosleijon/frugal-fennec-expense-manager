import { MenuItem, TextField } from "@material-ui/core"
import { Account } from "modules/accounts/types/Account"

interface AccountFormFieldProps {
  accounts: Account[],
  selectedAccountId: string | undefined,
  onSelectAccount: (selectedAccountId: string) => void
}

export function AccountFormField(props: AccountFormFieldProps) {

  const onSelectAccount = (event: React.ChangeEvent<HTMLInputElement>) => {
    props.onSelectAccount(event.target.value)
  }

  return (
    <TextField
      id="account-field"
      select
      label="Account"
      value={props.selectedAccountId ?? props.accounts[0]?.id ?? ""}
      variant="outlined"
      onChange={onSelectAccount}
      className="inputField"
    >
      {props.accounts.map((account) => (
        <MenuItem key={account.id} value={account.id}>
          {account.name}
        </MenuItem>
      ))}
    </TextField>
  )
}
