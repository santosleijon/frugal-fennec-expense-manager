import { accountsApi } from "api/accountsApi";
import { Action } from "reducers/Action";

export const GET_ACCOUNTS_RESULT_ACTION_TYPE = 'GET_ACCOUNTS_RESULT';

export async function getAccounts(): Promise<Action> {
  const accounts = await accountsApi.getAll()
  
  return {
    type: GET_ACCOUNTS_RESULT_ACTION_TYPE,
    payload: accounts,
  }
}