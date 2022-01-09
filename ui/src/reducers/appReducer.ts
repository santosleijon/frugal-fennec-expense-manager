import { ADD_ACCOUNT_ACTION_TYPE } from 'commands/addAccount';
import { GET_ACCOUNTS_ACTION_TYPE } from 'commands/getAccounts';
import { Account } from 'types/Account.js';
import { Action } from './Action';
import _ from 'lodash'
import { DELETE_ACCOUNT_ACTION_TYPE } from 'commands/deleteAccount';
import { ADD_EXPENSE_ACTION_TYPE } from 'commands/addExpense';

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
    case DELETE_ACCOUNT_ACTION_TYPE:
      return { ...state, accounts: _.filter(state.accounts, (account: Account) => account.id !== action.payload)}
    case ADD_EXPENSE_ACTION_TYPE:
      return { accounts: replaceAccount(action.payload, state.accounts) }
    default:
    return state
  }
}

function sortedAccounts(accounts: Account[]): Account[] {
  return _.sortBy(accounts, (account: Account) => account.name)
}

function replaceAccount(account: Account, accounts: Account[]): Account[] {
  return accounts.map((a) => {
    if (a.id === account.id) {
      return account
    }
    return a
  })
}
