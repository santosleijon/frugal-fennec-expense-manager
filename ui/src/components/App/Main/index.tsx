import { Route, Routes } from "react-router-dom";
import Expenses from './Expenses'
import Accounts from './Accounts'

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