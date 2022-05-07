import { accountsApi } from "modules/accounts/api/accountsApi";
import { Action } from "modules/common/reducers/Action";

export const UPDATE_ACCOUNT_NAME_ACTION_TYPE = 'UPDATE_ACCOUNT_NAME_RESULT';

export async function updateAccountName(accountId: string, newName: string): Promise<Action> {
  await accountsApi.updateAccountName(accountId, newName)
  
  return {
    type: UPDATE_ACCOUNT_NAME_ACTION_TYPE,
    payload: { accountId: accountId, newName: newName },
  }
}
