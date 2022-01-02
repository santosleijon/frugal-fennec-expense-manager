import { accountsApi } from "api/accountsApi";
import { Action } from "reducers/Action";
import { Account } from "types/Account";

export const ADD_ACCOUNT_ACTION_TYPE = 'ADD_ACCOUNT';

export async function addAccount(account: Account): Promise<Action> {
  const addedAccount = await accountsApi.add(account)
  
  return {
    type: ADD_ACCOUNT_ACTION_TYPE,
    payload: addedAccount,
  }
}
