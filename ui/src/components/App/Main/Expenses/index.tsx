import ExpensesDataGrid from "./ExpensesDataGrid"
import './index.css'
import { useState } from "react"
import AddExpenseForm from "./AddExpenseForm"
import { Account } from "types/Account"
import { Expense } from "types/Expense"
  
export default function Expenses() {
  // TODO: Read accounts from Redux state
  const ACCOUNTS = new Map<number, Account>([
    [3400, { id: "3400", name: "Bread" }],
    [3300, { id: "3300", name: "Dairy" }],
    [3800, { id: "3800", name: "Root fruits" }],
  ])

  const accountsList: Account[] = []
  ACCOUNTS.forEach(account => accountsList.push(account))

  const initialExpenses: Expense[] = [
    { id: 1, date: '2021-05-22', account: ACCOUNTS.get(3400)!, description: 'Bread', amount: 199.00 },
    { id: 2, date: '2021-05-22', account: ACCOUNTS.get(3300)!, description: 'Milk', amount: 49.00 },
    { id: 3, date: '2021-05-23', account: ACCOUNTS.get(3800)!, description: 'Potatoes', amount: 25.00 },
  ]

  const [expenses, setExpenses] = useState(initialExpenses)

  const onExpenseAdded = (newExpense: Expense) => {
    setExpenses(expenses.concat({ ...newExpense, id: expenses.length+1 }))
  }

  const onDeleteExpenses = (expenseIds: number[]) => {
    setExpenses(expenses.filter(t => !expenseIds.includes(t.id)))
  }

  return (
    <>
      <h1>Expenses</h1>

      <AddExpenseForm accounts={accountsList} onExpenseAdded={onExpenseAdded}/>

      <ExpensesDataGrid
        expenses={expenses}
        accounts={accountsList}
        onDeleteExpenses={onDeleteExpenses}
      />
    </>
  )
}