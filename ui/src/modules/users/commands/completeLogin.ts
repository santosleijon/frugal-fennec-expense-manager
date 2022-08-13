import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const COMPLETE_LOGIN_RESULT_ACTION_TYPE = 'COMPLETE_LOGIN_RESULT';

export async function completeLogin(email: string, verificationCode: string): Promise<Action> {
  return usersApi.completeLogin(email, verificationCode).then(() => {
    return {
        type: COMPLETE_LOGIN_RESULT_ACTION_TYPE,
      }
  })
}
