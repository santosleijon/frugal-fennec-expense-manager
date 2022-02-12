import { Action } from "reducers/Action";
import { Dispatch } from "redux"
import { failCommandAction } from "./failCommandAction";
import { completeCommandAction } from "./completeCommandAction";
import { startCommandAction } from "./startCommandAction"

// TODO: Restructure UI project to separate account specific files from common structure files like this one

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