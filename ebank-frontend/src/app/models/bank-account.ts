
import { Customer } from './customer';

export interface BankAccount {
  id: number;
  rib: string; // RIB
  balance: number; // Solde
  //status: AccountStatus; // Statut (Ouvert, Fermé, etc.)
  customer: Customer; // Référence au client
}
