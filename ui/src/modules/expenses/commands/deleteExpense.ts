import { accountsApi } from "modules/accounts/api/accountsApi"
import { Action } from "modules/common/reducers/Action";
import { Expense } from "../types/Expense";

export const DELETE_EXPENSE_RESULT_ACTION_TYPE = 'DELETE_EXPENSE_RESULT';

export async function deleteExpense(accountId: string, expense: Expense): Promise<Action> {
  const updatedAccount = await accountsApi.deleteExpense(accountId, expense)
  
  return {
    type: DELETE_EXPENSE_RESULT_ACTION_TYPE,
    payload: updatedAccount,
  }
}