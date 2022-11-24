import { Container } from "@material-ui/core";
import Accounts from "modules/accounts/components/Accounts/Accounts";
import { dispatchCommand } from "modules/common/commands/dispatchCommand";
import { AppState } from "modules/common/reducers/appReducer";
import Expenses from "modules/expenses/components/Expenses/Expenses";
import Reports from "modules/reports/components/Reports";
import { getCurrentUserSession } from "modules/users/commands/getCurrentUserSession";
import Login from "modules/users/components/Login/Login";
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
    <Container fixed>
      <Routes>
        { userIsLoggedIn ? (
          <>
            <Route path="/" element={<Expenses />} />
            <Route path="accounts" element={<Accounts />} />
            <Route path="reports" element={<Reports />} />
          </>
        ) : (   
            <Route path="/" element={<Login />} />
        ) }
      </Routes>
    </Container>
  )
}
