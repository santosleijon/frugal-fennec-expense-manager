import TransactionsDataGrid from "./TransactionsDataGrid"
import { Account, Transaction } from "./types"
import './index.css'
import { useState } from "react"
import AddTransactionForm from "./AddTransactionForm"
  
export default function Main() {
  const ACCOUNTS = new Map<number, Account>([
    [3400, { number: 3400, name: "Bread" }],
    [3300, { number: 3300, name: "Dairy" }],
    [3800, { number: 3800, name: "Root fruits" }],
  ])

  const accountsList: Account[] = []
  ACCOUNTS.forEach(account => accountsList.push(account))

  const initalTransactions: Transaction[] = [
    { id: 1, date: '2021-05-22', account: ACCOUNTS.get(3400)!, description: 'Bread', amount: 199.00 },
    { id: 2, date: '2021-05-22', account: ACCOUNTS.get(3300)!, description: 'Milk', amount: 49.00 },
    { id: 3, date: '2021-05-23', account: ACCOUNTS.get(3800)!, description: 'Potatoes', amount: 25.00 },
  ]

  const [transactions, setTransactions] = useState(initalTransactions)

  const onTransactionAdded = (newTransaction: Transaction) => {
    setTransactions(transactions.concat({ ...newTransaction, id: transactions.length+1 }))
  }

  const onDeleteTransactions = (transactionIds: number[]) => {
    setTransactions(transactions.filter(t => !transactionIds.includes(t.id)))
  }

  return (
    <div className="MainContainer">
      <h1>Transactions</h1>

      <AddTransactionForm accounts={accountsList} onTransactionAdded={onTransactionAdded}/>

      <TransactionsDataGrid
        transactions={transactions}
        onDeleteTransactions={onDeleteTransactions}
      />
    </div>
  )
}