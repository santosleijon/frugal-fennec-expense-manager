import { Account } from "types/Account";

export const accountsApi = {
  async add(account: Account): Promise<Account> {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/account`, {
      method: 'POST',
      headers: {
        'Accept': '*/*',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(account),
    })
  
    if (!response.ok) {
      throw new Error(`Failed to add account (HTTP status = ${response.status})`);
    }
  
    return response.json()
  },

  async getAll(): Promise<Account[]> {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/account`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      },
    })
  
    if (!response.ok) {
      throw new Error(`Failed to retrieve accounts (HTTP status = ${response.status})`);
    }
  
    return response.json()
  }
}