import Accounts from "modules/accounts/components/Accounts";
import { AppState } from "modules/common/reducers/appReducer";
import Expenses from "modules/expenses/components/Expenses";
import Reports from "modules/reports/components";
import Login from "modules/users/components/Login";
import { User } from "modules/users/types/User";
import { useSelector } from "react-redux";
import { Route, Routes } from "react-router-dom";

export default function Main() {
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
