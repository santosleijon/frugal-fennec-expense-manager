
import { Account } from "types/Account"
import { Button, Card, CardContent } from "@material-ui/core";
import { DataGrid, GridColDef, GridRowId } from "@mui/x-data-grid";
import { useState } from "react";
import { deleteAccount } from "commands/deleteAccount";
import { useDispatch } from "react-redux";
import { dispatchCommand } from "commands/dispatchCommand";

interface AccountsDataGridProps {
  accounts: Account[]
}

export function AccountsDataGrid(props: AccountsDataGridProps) {
  const dispatch = useDispatch()

  const [selectedRows, setSelectedRows] = useState<GridRowId[]>([])

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

  const onDeleteAccounts = () => {
    selectedRows.forEach((accountId) => {
      dispatchCommand(() => deleteAccount(accountId.toString()), dispatch)
    })
  }

  return (
    <Card>
      <CardContent>
        <h3>Accounts</h3>
        <div id="accountsDataGrid" style={{height: "400px", marginBottom: "12px" }}>
          <DataGrid
            rows={rows}
            columns={columns}
            disableSelectionOnClick
            disableColumnSelector
            checkboxSelection
            onSelectionModelChange={(newSelectionModel) => {
              setSelectedRows(newSelectionModel);
            }}
          />
        </div>
        <Button
          variant="contained"
          color="secondary"
          onClick={onDeleteAccounts}
        >
          Delete accounts
        </Button>
      </CardContent>
    </Card>
  )
}