import { ADD_ACCOUNT_ACTION_TYPE } from 'actions/addAccountAction';
import { GET_ACCOUNTS_ACTION_TYPE } from 'actions/getAccountsAction';
import { Account } from 'types/Account.js';
import { Action } from './Action';
import _ from 'lodash'

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
    case ADD_ACCOUNT_ACTION_TYPE:
      return { ...state, accounts: sortedAccounts([...state.accounts, action.payload]) }
    default:
      return state
  }
}

function sortedAccounts(accounts: Account[]): Account[] {
  return _.sortBy(accounts, function(account: Account) {
    return account.name
  })
}