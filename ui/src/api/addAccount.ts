import { Account } from "types/Account";

export const addAccount = async(account: Account): Promise<Account> => {
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
}