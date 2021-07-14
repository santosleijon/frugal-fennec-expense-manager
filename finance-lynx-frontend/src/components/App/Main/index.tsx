import TransactionsDataGrid from "./TransactionsDataGrid"
import { Account, Transaction } from "./types"
import './index.css'
  
export default function Main() {

  const ACCOUNTS = new Map<number, Account>([
    [3400, { number: 3400, name: "Bread" }],
    [3300, { number: 3300, name: "Dairy" }],
    [3800, { number: 3800, name: "Root fruits" }],
  ])

  const transactions: Transaction[] = [
    { id: 1, date: '2021-05-22', account: ACCOUNTS.get(3400)!, description: 'Bread', amount: 199.00 },
    { id: 2, date: '2021-05-22', account: ACCOUNTS.get(3300)!, description: 'Milk', amount: 49.00 },
    { id: 3, date: '2021-05-23', account: ACCOUNTS.get(3800)!, description: 'Potatoes', amount: 25.00 },
  ]

  return (
    <div className="MainContainer">
      <h1>Transactions</h1>
      <TransactionsDataGrid transactions={transactions} />
    </div>
  )
}