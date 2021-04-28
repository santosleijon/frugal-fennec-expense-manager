import { render, screen } from '@testing-library/react';
import App from './App';

test('renders welcome message', () => {
  render(<App />);
  const welcomeMessage = screen.getByText(/welcome/i);
  expect(welcomeMessage).toBeInTheDocument();
});
