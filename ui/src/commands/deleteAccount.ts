import { accountsApi } from "api/accountsApi";
import { Action } from "reducers/Action";

export const DELETE_ACCOUNT_ACTION_TYPE = 'DELETE_ACCOUNT';

export async function deleteAccount(accountId: string): Promise<Action> {
  await accountsApi.delete(accountId)

  return {
    type: DELETE_ACCOUNT_ACTION_TYPE,
    payload: accountId,
  }
}