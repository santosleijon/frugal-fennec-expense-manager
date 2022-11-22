import { usersApi } from "modules/users/api/usersApi"
import { Action } from "modules/common/reducers/Action";

export const DELETE_USER_RESULT_ACTION_TYPE = 'DELETE_USER_RESULT';

export async function deleteUser(): Promise<Action> {
  await usersApi.deleteUser()

  return {
    type: DELETE_USER_RESULT_ACTION_TYPE,
  }
}
