
import { Customer } from './customer';

export interface BankAccount {
  id: number;
  rib: string;
  balance: number;
  status: AccountStatus;
  customerId: number; // Référence au client
}
export enum AccountStatus {
  OPEN = 'OPEN',
  CLOSED = 'CLOSED'
}
