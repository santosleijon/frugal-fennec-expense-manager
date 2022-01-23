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
  
export default function Expenses() {
  const dispatch = useDispatch();

  useEffect(() => {
    // TODO: Handle "isLoading" state
    getAccounts().then(successAction => dispatch(successAction))
  }, [dispatch]);

  const accounts = useSelector<AppState, Account[]>(
    (state) => state.accounts
  );

  const onExpenseAdded = (accountId: string, newExpense: Expense) => {
    addExpense(accountId, newExpense).then((successAction) => dispatch(successAction))
  }

  const onDeleteExpense = (accountId: string, expense: Expense) => {
    deleteExpense(accountId, expense).then((successAction) => dispatch(successAction))
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