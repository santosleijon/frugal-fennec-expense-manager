import ExpensesDataGrid from "./ExpensesDataGrid"
import './index.css'
import { useEffect } from "react"
import AddExpenseForm from "./AddExpenseForm"
import { useDispatch, useSelector } from "react-redux"
import { deleteExpense } from "modules/expenses/commands/deleteExpense"
import { dispatchCommand } from "modules/common/commands/dispatchCommand"
import { addExpense } from "modules/expenses/commands/addExpense"
import { AppState } from "modules/common/reducers/appReducer"
import { Expense } from "modules/expenses/types/Expense"
import { getAccounts } from "modules/accounts/commands/getAccounts"
import { Account } from "modules/accounts/types/Account"
  
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