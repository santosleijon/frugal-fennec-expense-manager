import { render, screen } from '@testing-library/react';
import App from '.';

test('renders Expenses header', () => {
  render(<App />);
  const header = screen.getAllByText(/Expenses/i)[0];
  expect(header).toBeInTheDocument();
});
