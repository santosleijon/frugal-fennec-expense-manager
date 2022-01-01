import ExpensesDataGrid from "./ExpensesDataGrid"
import './index.css'
import { useEffect } from "react"
import AddExpenseForm from "./AddExpenseForm"
import { Account } from "types/Account"
import { Expense } from "types/Expense"
import { useDispatch, useSelector } from "react-redux"
import { AppState } from "reducers/appReducer"
import { getAccountsAction } from "actions/getAccountsAction"
  
export default function Expenses() {
  const dispatch = useDispatch();

  useEffect(() => {
    // TODO: Handle "isLoading" state
    getAccountsAction().then(successAction => dispatch(successAction))
  }, [dispatch]);

  const accounts = useSelector<AppState, Account[]>(
    (state) => state.accounts
  );

  const onExpenseAdded = (newExpense: Expense) => {
    //setExpenses(expenses.concat({ ...newExpense, id: expenses.length+1 }))
  }

  const onDeleteExpenses = (expenseIds: number[]) => {
    //setExpenses(expenses.filter(t => !expenseIds.includes(t.id)))
  }

  return (
    <>
      <h1>Expenses</h1>

      <AddExpenseForm accounts={accounts} onExpenseAdded={onExpenseAdded}/>

      <ExpensesDataGrid
        accounts={accounts}
        onDeleteExpenses={onDeleteExpenses}
      />
    </>
  )
}