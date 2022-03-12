import { Dispatch } from "redux"
import { failCommandAction } from "./failCommandAction";
import { completeCommandAction } from "./completeCommandAction";
import { startCommandAction } from "./startCommandAction"
import { Action } from "modules/common/reducers/Action";

export async function dispatchCommand(command: () => Promise<Action>, dispatch: Dispatch) {
    dispatch(startCommandAction)

    try {
        const commandResult = await command()

        dispatch(commandResult)

        dispatch(completeCommandAction)
    } catch (e) {
        if (e instanceof Error && e.message) {
            dispatch(failCommandAction(e.message))
        } else {
            dispatch(failCommandAction("An unknown error occured"))

        }
    }
}