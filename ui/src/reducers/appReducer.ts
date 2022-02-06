import { ADD_ACCOUNT_RESULT_ACTION_TYPE } from 'commands/addAccount';
import { GET_ACCOUNTS_RESULT_ACTION_TYPE } from 'commands/getAccounts';
import { Account } from 'types/Account.js';
import { Action } from './Action';
import _ from 'lodash'
import { DELETE_ACCOUNT_RESULT_ACTION_TYPE } from 'commands/deleteAccount';
import { ADD_EXPENSE_RESULT_ACTION_TYPE } from 'commands/addExpense';
import { DELETE_EXPENSE_RESULT_ACTION_TYPE } from 'commands/deleteExpense';
import { START_COMMAND_ACTION_TYPE } from 'commands/startCommandAction';
import { COMPLETE_COMMAND_ACTION_TYPE } from 'commands/completeCommandAction';

export interface AppState {
  isLoadingCommand: boolean,
  accounts: Account[]
}

const initialState: AppState = {
  isLoadingCommand: false,
  accounts: []
}

export function appReducer(state: AppState = initialState, action: Action) {
  switch (action.type) {
    case START_COMMAND_ACTION_TYPE:
      return { ...state, isLoadingCommand: true }
    case COMPLETE_COMMAND_ACTION_TYPE:
      return { ...state, isLoadingCommand: false }
    case GET_ACCOUNTS_RESULT_ACTION_TYPE:
      return { ...state, accounts: action.payload }
    case ADD_ACCOUNT_RESULT_ACTION_TYPE:
      return { ...state, accounts: sortedAccounts([...state.accounts, action.payload]) }
    case DELETE_ACCOUNT_RESULT_ACTION_TYPE:
      return { ...state, accounts: _.filter(state.accounts, (account: Account) => account.id !== action.payload)}
    case ADD_EXPENSE_RESULT_ACTION_TYPE:
      return { ...state, accounts: replaceAccount(action.payload, state.accounts) }
    case DELETE_EXPENSE_RESULT_ACTION_TYPE:
      return { ...state, accounts: replaceAccount(action.payload, state.accounts) }
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
