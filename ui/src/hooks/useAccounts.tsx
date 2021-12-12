import { fetchAccounts } from "api/fetchAccounts";
import { useEffect, useState } from "react";
import { Account } from "types/Account";

export default function useAccounts() {
  const [accounts, setAccounts] = useState<Account[]>([])

  useEffect(() => {
    const fetchAndSetAccounts = async() => {
      const response = await fetchAccounts()
      setAccounts(response)
    }

    fetchAndSetAccounts()
  }, [fetchAccounts])

  return accounts
}