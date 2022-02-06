import ExpensesDataGrid from "./ExpensesDataGrid"
import './index.css'
import { useEffect } from "react"
import AddExpenseForm from "./AddExpenseForm"
import { Account } from "types/Account"
import { Expense } from "types/Expense"
import { useDispatch, useSelector } from "react-redux"
import { AppState } from "reducers/appReducer"
import { getAccounts } from "commands/getAccounts"
import { addExpense } from "commands/addExpense"
import { deleteExpense } from "commands/deleteExpense"
import { dispatchCommand } from "commands/dispatchCommand"
  
export default function Expenses() {
  const dispatch = useDispatch();

  useEffect(() => {
    dispatchCommand(getAccounts, dispatch)
  }, [dispatch]);

  const accounts = useSelector<AppState, Account[]>(
    (state) => state.accounts
  );

  const onExpenseAdded = (accountId: string, newExpense: Expense) => {
    dispatchCommand(() => addExpense(accountId, newExpense), dispatch)
  }

  const onDeleteExpense = (accountId: string, expense: Expense) => {
    dispatchCommand(() => deleteExpense(accountId, expense), dispatch)
  }

  return (
    <>
      <h1>Expenses</h1>

      <AddExpenseForm accounts={accounts} onExpenseAdded={onExpenseAdded} />

      <ExpensesDataGrid
        accounts={accounts}
        onDeleteExpenses={onDeleteExpense}
      />
    </>
  )
}