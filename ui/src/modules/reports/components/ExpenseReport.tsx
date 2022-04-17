import { ArgumentAxis, Chart, ValueAxis } from "@devexpress/dx-react-chart-material-ui";
import { Box, Paper } from "@material-ui/core";
import { LineSeries, ValueScale } from "@devexpress/dx-react-chart";
import { formatDate } from "utils/formatDate";
import { Account } from "modules/accounts/types/Account";
import { Expense } from "modules/expenses/types/Expense";
import { groupBy } from "lodash";
import _ from "lodash";

interface ExpenseReportProps {
    account: Account,
}

type TotalExpenseByDate = {
  date: string,
  amount: number,
}

export function ExpenseReport(props: ExpenseReportProps) {
  if (props.account.expenses.length < 1) {
    return (
      <Paper>
        <Box sx={{ fontStyle: 'italic', p: 2 }}>
          Selected account has no expenses.
        </Box>
      </Paper>
    )
  }

  const totalExpensesByDate: TotalExpenseByDate[] = getTotalExpensesByDate(props.account)

  return (
    <Paper>
      <Chart data={totalExpensesByDate}>
        <ValueScale name="amount" />

        <ArgumentAxis />

        <ValueAxis
          scaleName="amount"
          position="left"
          showGrid={true}
          showLine={true}
          showTicks={true}
        />

        <LineSeries
          name="Total amount"
          valueField="amount"
          argumentField="date"
          scaleName="amount"
        />
      </Chart>
    </Paper>
  )
}

function getTotalExpensesByDate(account: Account): TotalExpenseByDate[] {
  const expensesByDate = groupBy(account.expenses, (expense: Expense) =>
    formatDate(expense.date)
  )

  var totalExpensesByDate: TotalExpenseByDate[] = []

  for (const date in expensesByDate) {
    const amountSum = expensesByDate[date].reduce((sum, expense) => sum + expense.amount, 0)

    totalExpensesByDate.push({ date: date, amount: amountSum })
  }

  return _.sortBy(totalExpensesByDate, (i: TotalExpenseByDate) => i.date)
}
