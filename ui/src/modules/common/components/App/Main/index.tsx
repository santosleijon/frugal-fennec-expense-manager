import Accounts from "modules/accounts/components/Accounts";
import Expenses from "modules/expenses/components/Expenses";
import { Route, Routes } from "react-router-dom";

export default function Main() {
  return (
    <div className="MainContainer">
      <Routes>
        <Route path="/" element={<Expenses />} />
        <Route path="accounts" element={<Accounts />} />
      </Routes>
    </div>
  )
}