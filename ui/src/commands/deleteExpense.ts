import { accountsApi } from "api/accountsApi"
import { Action } from "reducers/Action";
import { Expense } from "types/Expense"

export const DELETE_EXPENSE_ACTION_TYPE = 'DELETE_EXPENSE';

export async function deleteExpense(accountId: string, expense: Expense): Promise<Action> {
  const updatedAccount = await accountsApi.deleteExpense(accountId, expense)
  
  return {
    type: DELETE_EXPENSE_ACTION_TYPE,
    payload: updatedAccount,
  }
}