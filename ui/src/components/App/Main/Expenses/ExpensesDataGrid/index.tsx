import { Button, Card, CardContent } from "@material-ui/core";
import { DataGrid, GridColDef, GridRowId, GridRowsProp } from '@mui/x-data-grid';
import DataGridContainer from "components/App/common/DataGridContainer";
import * as React from 'react';
import { useState } from "react";
import { Account } from "types/Account";
import { Expense } from "types/Expense";
import { formatAmount } from "utils/formatAmount";
import { formatDate } from "utils/formatDate";

interface ExpensesDataGridProps {
    accounts: Account[]
    onDeleteExpenses: (accountId: string, expense: Expense) => void
}

export default function ExpensesDataGrid(props: ExpensesDataGridProps) {
  const columns: GridColDef[] = [
    {
      field: 'date',
      headerName: 'Date',
      width: 200,
      type: 'date',
    },
    {
      field: 'account',
      headerName: 'Account',
      width: 200,
    },
    {
      field: 'description',
      headerName: 'Description',
      width: 250,
      type: 'string',
    },
    {
      field: 'amount',
      headerName: 'Amount',
      width: 200,
      type: 'number',
    },
  ];

  const accountsWithExpenses = props.accounts.filter((account) => { return (account.expenses.length > 0) })

  const rows: GridRowsProp = accountsWithExpenses.flatMap((account) => {
    return account.expenses.map((expense, index) => {
        return {
          id: `${account.id}-${index}`,
          accountId: account.id,
          expense: expense,
          date: formatDate(expense.date),
          account: account.name,
          description: expense.description,
          amount: formatAmount(expense.amount),
        }
      })
  })

  const [selectionModel, setSelectionModel] = useState<GridRowId[]>([])

  const onDeleteExpenses = () => {
    selectionModel.forEach((value, index, array) => {
      const row = rows.find((row) => row.id === value)
      const accountId = row?.accountId
      const expense = row?.expense

      if (accountId && expense) {
        props.onDeleteExpenses(accountId, expense)
      }
    })
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
