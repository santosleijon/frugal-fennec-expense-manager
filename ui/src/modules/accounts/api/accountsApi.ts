import { Account } from "modules/accounts/types/Account";
import { Expense } from "modules/expenses/types/Expense";

const baseUrl = `${process.env.REACT_APP_API_URL}/account`

export const accountsApi = {
  
  async getAll(): Promise<Account[]> {
    const response = await fetch(baseUrl, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      },
      credentials: "include",
    })
    
    if (!response.ok) {
      throw new Error(`Failed to retrieve accounts (HTTP status = ${response.status})`);
    }
    
    return response.json()
  },
  
  async add(account: Account): Promise<Account> {
    const response = await fetch(`${baseUrl}/`, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      credentials: "include",
      body: JSON.stringify(account),
    })
  
    if (!response.ok) {
      throw new Error(`Failed to add account (HTTP status = ${response.status})`);
    }
  
    return response.json()
  },

  async updateAccountName(accountId: string, newName: string): Promise<void> {
    const response = await fetch(`${baseUrl}/${accountId}`, {
      method: 'PATCH',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      credentials: "include",
      body: JSON.stringify({ "newName": newName }),
    })

    if (!response.ok) {
      throw new Error(`Failed to update account name (HTTP status = ${response.status})`);
    }
  },

  async delete(accountId: string): Promise<void> {
    const response = await fetch(`${baseUrl}/${accountId}`, {
      method: 'DELETE',
      credentials: "include",
    })

    if (!response.ok) {
      throw new Error(`Failed to delete account (HTTP status = ${response.status})`);
    }
  },

  async addExpense(accountId: string, expense: Expense): Promise<Expense> {
    const response = await fetch(`${baseUrl}/${accountId}/expense/`, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      credentials: "include",
      body: JSON.stringify(expense),
    })
  
    if (!response.ok) {
      throw new Error(`Failed to add expense (HTTP status = ${response.status})`);
    }

    return response.json()
  },

  async deleteExpense(accountId: string, expense: Expense): Promise<void> {
    const response = await fetch(`${baseUrl}/${accountId}/expense/`, {
      method: 'DELETE',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      credentials: "include",
      body: JSON.stringify(expense),
    })
  
    if (!response.ok) {
      throw new Error(`Failed to delete expense (HTTP status = ${response.status})`);
    }

    return response.json()
  }
}