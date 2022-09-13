import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const LOGOUT_RESULT_ACTION_TYPE = 'LOGOUT_RESULT';

export async function logout(): Promise<Action> {
  await usersApi.logout()

  return {
    type: LOGOUT_RESULT_ACTION_TYPE,
  }
}
