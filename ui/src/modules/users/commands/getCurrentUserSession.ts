import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const GET_CURRENT_USER_SESSION_RESULT_ACTION_TYPE = 'GET_CURRENT_USER_SESSION_RESULT';

export async function getCurrentUserSession(): Promise<Action> {
  const currentUserSession = await usersApi.getCurrentUserSession()

  return {
    type: GET_CURRENT_USER_SESSION_RESULT_ACTION_TYPE,
    payload: currentUserSession,
  }
}
