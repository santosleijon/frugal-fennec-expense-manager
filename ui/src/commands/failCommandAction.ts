import { Action } from "reducers/Action";

export const FAIL_COMMAND_ACTION_TYPE = 'FAIL_COMMAND';

export function failCommandAction(errorMessage: string): Action {
  return {
    type: FAIL_COMMAND_ACTION_TYPE,
    payload: errorMessage,
  }
}
