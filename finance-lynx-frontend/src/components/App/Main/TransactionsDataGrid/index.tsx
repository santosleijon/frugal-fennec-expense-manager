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
      width: 150,
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
      width: 400,
      editable: true,
    },
    {
      field: 'amount',
      headerName: 'Amount',
      type: 'amount',
      width: 200,
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
        amount: transaction.amount.toFixed(2) + " SEK"
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