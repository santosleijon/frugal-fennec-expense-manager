import { Account } from "types/Account";

const baseUrl = `${process.env.REACT_APP_API_URL}/account`

export const accountsApi = {
  
  async getAll(): Promise<Account[]> {
    const response = await fetch(baseUrl, {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      },
    })
    
    if (!response.ok) {
      throw new Error(`Failed to retrieve accounts (HTTP status = ${response.status})`);
    }
    
    return response.json()
  },
  
  async add(account: Account): Promise<Account> {
    const response = await fetch(baseUrl, {
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

  async delete(accountId: string): Promise<void> {
    const response = await fetch(`${baseUrl}/${accountId}`, {
      method: 'DELETE',
    })

    if (!response.ok) {
      throw new Error(`Failed to delete account (HTTP status = ${response.status})`);
    }

    return
  }
}