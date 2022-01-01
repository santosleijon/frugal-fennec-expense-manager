import { Button, Card, CardContent, FormControl, FormHelperText, MenuItem } from "@material-ui/core";
import Select, { SelectChangeEvent } from '@mui/material/Select';
import { DataGrid, GridColDef, GridRenderCellParams, GridRowId, GridRowsProp } from '@mui/x-data-grid';
import DataGridContainer from "components/App/common/DataGridContainer";
import * as React from 'react';
import { useState } from "react";
import { Account } from "types/Account";

interface ExpensesDataGridProps {
    accounts: Account[]
    onDeleteExpenses: (expenseIds: number[]) => void
}

export default function ExpensesDataGrid(props: ExpensesDataGridProps) {
  function AccountEditCell(props: GridRenderCellParams<number> & { accounts: Account[] }): JSX.Element {
    const onChange = (event: SelectChangeEvent) => {
      const newSelectedAccountId = +event.target.value
      console.log("newSelectedAccountId=", newSelectedAccountId)
    }

    const selectedAccountId = props.accounts.find((a: Account) => a.name === props.row.account)?.id
  
    return (
      <FormControl variant="filled">
        <Select
          id="account-field-{selectedAccountId}"
          displayEmpty
          inputProps={{ 'aria-label': 'Account' }}
          value={selectedAccountId}
          variant="outlined"
          onChange={onChange}
          className="inputField"
        >
          {props.accounts.map((account) => (
            <MenuItem key={account.id} value={account.id}>
              {account.name}
            </MenuItem>
          ))}
        </Select>
        <FormHelperText>Account</FormHelperText>
      </FormControl>
    );
  }

  function renderAccountEditCell(params: any) {
    return <AccountEditCell {...params} accounts={props.accounts} />;
  }

  const columns: GridColDef[] = [
    {
      field: 'date',
      headerName: 'Date',
      width: 200,
      type: 'date',
      editable: true,
    },
    {
      field: 'account',
      headerName: 'Account',
      width: 200,
      editable: true,
      renderEditCell: renderAccountEditCell,
    },
    {
      field: 'description',
      headerName: 'Description',
      width: 250,
      type: 'string',
      editable: true,
    },
    {
      field: 'amount',
      headerName: 'Amount',
      width: 200,
      type: 'number',
      editable: true,
    },
  ];

  const rows: GridRowsProp = props.accounts.filter((account) => { return (account.expenses.length > 0) }).flatMap((account) => {
      return account.expenses.map((expense, index) => {
        return {
          id: `${account.id}-${index}`,
          date: formatDate(expense.date),
          account: account.name,
          description: expense.description,
          amount: formatAmount(expense.amount),
        }
      })
  })

  const [selectionModel, setSelectionModel] = useState<GridRowId[]>([])

  const onDeleteExpenses = () => {
    props.onDeleteExpenses(selectionModel.map(rowId => +rowId))
  }

  return (
    <Card>
      <CardContent>
        <h3>Expenses</h3>
        <DataGridContainer>
          <DataGrid
            rows={rows}
            columns={columns}
            disableSelectionOnClick
            disableColumnSelector
            checkboxSelection
            onSelectionModelChange={(newSelectionModel) => {
              setSelectionModel(newSelectionModel);
            }}
          />
        </DataGridContainer>
        <Button
          variant="contained"
          color="secondary"
          onClick={onDeleteExpenses}
        >
          Delete expenses
        </Button>
      </CardContent>
    </Card>
  )
}

function formatDate(date: string): string {
  return date.slice(0, 10)
}

function formatAmount(amount: number): string {
  return amount.toFixed(2)
}
