import { Expense } from "../../expenses/types/Expense";

export interface Account {
  id?: string
  name: string
  expenses: Expense[]
}