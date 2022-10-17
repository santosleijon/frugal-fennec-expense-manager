import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const ABORT_LOGIN_RESULT_ACTION_TYPE = 'ABORT_LOGIN_RESULT';

export async function abortLogin(email: string): Promise<Action> {
  return usersApi.abortLogin(email).then(() => {
    return {
        type: ABORT_LOGIN_RESULT_ACTION_TYPE,
      }
  })
}
