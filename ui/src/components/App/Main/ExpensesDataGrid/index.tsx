import * as React from 'react';
import { Button, Card, CardContent } from "@material-ui/core";
import { DataGrid, GridRowId, GridColDef } from "@material-ui/data-grid";
import { Expense } from "../types";
import './index.css'

interface ExpensesDataGridProps {
    expenses: Expense[]
    onDeleteExpenses: (expenseIds: number[]) => void
}

export default function ExpensesDataGrid(props: ExpensesDataGridProps) {
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

  const rows = props.expenses.map(expense => 
    {
      return {
        id: expense.id,
        date: expense.date,
        account: `${expense.account.number} - ${expense.account.name}`,
        description: expense.description,
        amount: expense.amount.toFixed(2)
      }
    }
  )

  const [selectionModel, setSelectionModel] = React.useState<GridRowId[]>([])

  const onDeleteExpenses = () => {
    props.onDeleteExpenses(selectionModel.map(rowId => +rowId))
  }

  return (
    <Card>
      <CardContent>
        <h3>Expenses</h3>
        <Button
          variant="contained"
          color="secondary"
          onClick={onDeleteExpenses}
        >
          Delete expenses
        </Button>
        <div className="ExpensesDataGridContainer">
          <DataGrid
            rows={rows}
            columns={columns}
            pageSize={10}
            disableSelectionOnClick
            checkboxSelection
            onSelectionModelChange={(newSelectionModel) => {
              setSelectionModel(newSelectionModel);
            }}
          />
        </div>
      </CardContent>
    </Card>
  )
}