import { addAccount } from "api/addAccount";
import { Action } from "reducers/Action";
import { Account } from "types/Account";

export const ADD_ACCOUNT_ACTION_TYPE = 'ADD_ACCOUNT';

export async function addAccountAction(account: Account): Promise<Action> {
  const addedAccount = await addAccount(account)
  
  return {
    type: ADD_ACCOUNT_ACTION_TYPE,
    payload: addedAccount,
  }
}
