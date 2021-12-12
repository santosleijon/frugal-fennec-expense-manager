import { Account } from "types/Account";

export const fetchAccounts = async(): Promise<Account[]> => {
  console.log(`${process.env.REACT_APP_API_URL}/account`)

  const response = await fetch(`${process.env.REACT_APP_API_URL}/account`, {
    method: 'GET',
    headers: {
      'Accept': 'application/json'
    },
  })

  if (!response.ok) {
    throw new Error(`Failed retrieve accounts (HTTP status = ${response.status})`);
  }

  return response.json()
}