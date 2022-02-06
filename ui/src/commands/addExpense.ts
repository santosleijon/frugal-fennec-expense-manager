import { accountsApi } from "api/accountsApi"
import { Action } from "reducers/Action";
import { Expense } from "types/Expense"

export const ADD_EXPENSE_RESULT_ACTION_TYPE = 'ADD_EXPENSE_RESULT';

export async function addExpense(accountId: string, expense: Expense): Promise<Action> {
  const updatedAccount = await accountsApi.addExpense(accountId, expense)
  
  return {
    type: ADD_EXPENSE_RESULT_ACTION_TYPE,
    payload: updatedAccount,
  }
}