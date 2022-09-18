import Accounts from "modules/accounts/components/Accounts";
import { dispatchCommand } from "modules/common/commands/dispatchCommand";
import { AppState } from "modules/common/reducers/appReducer";
import Expenses from "modules/expenses/components/Expenses";
import Reports from "modules/reports/components";
import { getCurrentUserSession } from "modules/users/commands/getCurrentUserSession";
import Login from "modules/users/components/Login";
import { User } from "modules/users/types/User";
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";

export default function Main() {
  const dispatch = useDispatch()

  useEffect(() => {
    dispatchCommand(() => getCurrentUserSession(), dispatch)
  }, [dispatch])

  const userIsLoggedIn = useSelector<AppState, User | null>(
    (state) => state.loggedInUser
  ) != null;

  return (
    <div className="MainContainer">
      <Routes>
        { userIsLoggedIn ? (
          <>
            <Route path="/" element={<Reports />} />
            <Route path="expenses" element={<Expenses />} />
            <Route path="accounts" element={<Accounts />} />
          </>
        ) : (
          <Route path="/" element={<Login />} />
        ) }
      </Routes>
    </div>
  )
}
