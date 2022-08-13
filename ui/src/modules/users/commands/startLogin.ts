import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const START_LOGIN_RESULT_ACTION_TYPE = 'START_LOGIN_RESULT';

export async function startLogin(email: string): Promise<Action> {
  await usersApi.startLogin(email)

  return {
    type: START_LOGIN_RESULT_ACTION_TYPE,
  }
}
