import { DataGrid, GridColDef } from "@material-ui/data-grid";
import { Transaction } from "../types";
import './index.css'

interface TransactionsDataGridProps {
    transactions: Transaction[]
}

export default function TransactionsDataGrid(props: TransactionsDataGridProps) {
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
      width: 350,
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

  const rows = props.transactions.map(transaction => 
    {
      return {
        id: transaction.id,
        date: transaction.date,
        account: `${transaction.account.number} - ${transaction.account.name}`,
        description: transaction.description,
        amount: transaction.amount.toFixed(2)
      }
    }
  )

  return (
    <div className="TransactionsDataGridContainer">
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={10}
        disableSelectionOnClick
      />
    </div>
  )
}