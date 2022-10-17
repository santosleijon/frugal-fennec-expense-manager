import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const COMPLETE_LOGIN_RESULT_ACTION_TYPE = 'COMPLETE_LOGIN_RESULT';

export async function completeLogin(email: string, verificationCode: string): Promise<Action> {
  const user = await usersApi.completeLogin(email, verificationCode)

  return {
    type: COMPLETE_LOGIN_RESULT_ACTION_TYPE,
    payload: user,
  }
}
