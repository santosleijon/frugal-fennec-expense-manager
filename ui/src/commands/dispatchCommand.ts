import { Action } from "reducers/Action";
import { Dispatch } from "redux"
import { completeCommandAction } from "./completeCommandAction";
import { startCommandAction } from "./startCommandAction"

export async function dispatchCommand(command: () => Promise<Action>, dispatch: Dispatch) {
    dispatch(startCommandAction)

    const commandResult = await command()

    dispatch(commandResult)

    dispatch(completeCommandAction)
}