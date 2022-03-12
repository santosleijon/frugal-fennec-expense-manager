import { accountsApi } from "modules/accounts/api/accountsApi";
import { Action } from "modules/common/reducers/Action";

export const DELETE_ACCOUNT_RESULT_ACTION_TYPE = 'DELETE_ACCOUNT_RESULT';

export async function deleteAccount(accountId: string): Promise<Action> {
  await accountsApi.delete(accountId)

  return {
    type: DELETE_ACCOUNT_RESULT_ACTION_TYPE,
    payload: accountId,
  }
}