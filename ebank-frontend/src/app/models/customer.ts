import {BankAccount} from "./bank-account";

export interface Customer {
  id: number;
  name: string;
  cin: string;
  email: string;
  birthday: Date;
  postalAddress: string;
  bankAccounts: BankAccount[]; // Vous devrez également créer l'interface BankAccount
}
