import { render, screen } from '@testing-library/react';
import App from '.';

test('renders Transactions header', () => {
  render(<App />);
  const transactionsHeader = screen.getByText(/Transactions/i);
  expect(transactionsHeader).toBeInTheDocument();
});
