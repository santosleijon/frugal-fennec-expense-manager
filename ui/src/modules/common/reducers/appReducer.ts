import { ADD_ACCOUNT_RESULT_ACTION_TYPE } from 'modules/accounts/commands/addAccount';
import { Action } from './Action';
import _ from 'lodash'
import { DELETE_EXPENSE_RESULT_ACTION_TYPE } from 'modules/expenses/commands/deleteExpense';
import { START_COMMAND_ACTION_TYPE } from 'modules/common/commands/startCommandAction';
import { COMPLETE_COMMAND_ACTION_TYPE } from 'modules/common/commands/completeCommandAction';
import { FAIL_COMMAND_ACTION_TYPE } from 'modules/common/commands/failCommandAction';
import { GET_ACCOUNTS_RESULT_ACTION_TYPE } from 'modules/accounts/commands/getAccounts';
import { DELETE_ACCOUNT_RESULT_ACTION_TYPE } from 'modules/accounts/commands/deleteAccount';
import { ADD_EXPENSE_RESULT_ACTION_TYPE } from 'modules/expenses/commands/addExpense';
import { Account } from 'modules/accounts/types/Account';
import { UPDATE_ACCOUNT_NAME_ACTION_TYPE } from 'modules/accounts/commands/updateAccountName';
import { RESET_COMMAND_ACTION_TYPE } from '../commands/resetCommandAction';
import { COMPLETE_LOGIN_RESULT_ACTION_TYPE } from 'modules/users/commands/completeLogin';
import { User } from 'modules/users/types/User';
import { LOGOUT_RESULT_ACTION_TYPE } from 'modules/users/commands/logout';

export interface AppState {
  isLoadingCommand: boolean,
  commandErrorMessage: string | null,
  loggedInUser: User | null
  accounts: Account[]
}

const initialState: AppState = {
  isLoadingCommand: false,
  commandErrorMessage: null,
  loggedInUser: null,
  accounts: []
}

export function appReducer(state: AppState = initialState, action: Action) {
  switch (action.type) {
    case START_COMMAND_ACTION_TYPE:
      return { ...state, isLoadingCommand: true }
    case FAIL_COMMAND_ACTION_TYPE:
      return { ...state, ...{ isLoadingCommand: false, commandErrorMessage: action.payload } }
    case COMPLETE_COMMAND_ACTION_TYPE:
      return { ...state, isLoadingCommand: false }
    case RESET_COMMAND_ACTION_TYPE:
      return { ...state, ...{ isLoadingCommand: false, commandErrorMessage: "" } }
    case GET_ACCOUNTS_RESULT_ACTION_TYPE:
      return { ...state, accounts: action.payload }
    case ADD_ACCOUNT_RESULT_ACTION_TYPE:
      return { ...state, accounts: sortedAccounts([...state.accounts, action.payload]) }
    case UPDATE_ACCOUNT_NAME_ACTION_TYPE:
      const account = state.accounts.find((account) => account.id === action.payload.accountId)!!
      const updatedAccount = { ...account, name: action.payload.newName }
      return { ...state, accounts: replaceAccount(updatedAccount, state.accounts) }
    case DELETE_ACCOUNT_RESULT_ACTION_TYPE:
      return { ...state, accounts: _.filter(state.accounts, (account: Account) => account.id !== action.payload)}
    case ADD_EXPENSE_RESULT_ACTION_TYPE:
      return { ...state, accounts: replaceAccount(action.payload, state.accounts) }
    case DELETE_EXPENSE_RESULT_ACTION_TYPE:
      return { ...state, accounts: replaceAccount(action.payload, state.accounts) }
    case COMPLETE_LOGIN_RESULT_ACTION_TYPE: 
      return { ...state, loggedInUser: action.payload }
    case LOGOUT_RESULT_ACTION_TYPE:
      return { ...state, loggedInUser: null }
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
