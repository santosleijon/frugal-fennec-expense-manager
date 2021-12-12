import { Button, Card, CardContent, FormControl, TextField } from "@material-ui/core";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import useAccounts from "hooks/useAccounts";
import { Account } from "types/Account";

export default function Accounts() {
  const accounts = useAccounts()

  return (
    <>
      <h1>Accounts</h1>
      <AddAccountForm />
      <AccountsDataGrid accounts={accounts} />
    </>
  )
}

function AddAccountForm() {
  const onNameChange = () => {}

  const onSubmit = () => {}

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
                variant="outlined"
                onChange={onNameChange}
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

interface AccountsDataGridProps {
  accounts: Account[]
}

function AccountsDataGrid(props: AccountsDataGridProps) {
  const columns: GridColDef[] = [
    {
      field: 'name',
      headerName: 'Name',
      type: 'string',
      editable: true,
    },
  ]

  const rows = props.accounts.map(account =>
    {
      return {
        id: account.id,
        name: account.name,
      }
    }
  )

  const onDeleteAccounts = () => {}
  
  return (
    <Card>
      <CardContent>
        <h3>Accounts</h3>
        <div style={{height: "400px", marginBottom: "12px" }}>
          <DataGrid
            rows={rows}
            columns={columns}
            disableSelectionOnClick
            disableColumnSelector
            checkboxSelection
          />
        </div>
        <Button
          variant="contained"
          color="secondary"
          onClick={onDeleteAccounts}
        >
          Delete expenses
        </Button>
      </CardContent>
    </Card>
  )
}