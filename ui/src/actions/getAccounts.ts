import { fetchAccounts } from "api/fetchAccounts";
import { Action } from "reducers/Action";

export const GET_ACCOUNTS_ACTION_TYPE = 'GET_ACCOUNTS';

export async function getAccounts(): Promise<Action> {
  const accounts = await fetchAccounts()
  
  return {
    type: GET_ACCOUNTS_ACTION_TYPE,
    payload: accounts,
  }
}