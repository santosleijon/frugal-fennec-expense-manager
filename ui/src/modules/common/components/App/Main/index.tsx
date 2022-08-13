import Accounts from "modules/accounts/components/Accounts";
import Expenses from "modules/expenses/components/Expenses";
import Reports from "modules/reports/components";
import Login from "modules/users/components/Login";
import { Route, Routes } from "react-router-dom";

export default function Main() {
  return (
    <div className="MainContainer">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="reports" element={<Reports />} />
        <Route path="expenses" element={<Expenses />} />
        <Route path="accounts" element={<Accounts />} />
      </Routes>
    </div>
  )
}