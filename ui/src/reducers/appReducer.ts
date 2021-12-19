import { GET_ACCOUNTS_ACTION_TYPE } from 'actions/getAccounts';
import { Account } from 'types/Account.js';
import { Action } from './Action';

export interface AppState {
  accounts: Account[]
}

const initialState: AppState = {
  accounts: []
}

export function appReducer(state: AppState = initialState, action: Action) {
  switch (action.type) {
    case GET_ACCOUNTS_ACTION_TYPE:
      return { accounts: action.payload }
    default:
      return state
  }
}
