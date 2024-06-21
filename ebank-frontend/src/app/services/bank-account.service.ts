import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { BankAccount } from '../models/bank-account';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BankAccountService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${this.apiUrl}/accounts`)
      .pipe(catchError(this.handleError));
  }

  getAccountById(id: number): Observable<BankAccount> {
    return this.http.get<BankAccount>(`${this.apiUrl}/acoounts/${id}`);
  }

  getAccountByRib(rib: string): Observable<BankAccount> {
    return this.http.get<BankAccount>(`${this.apiUrl}/acoounts/rib/${rib}`);
  }
  searchAccounts(keyword: string): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${this.apiUrl}/accounts/search?keyword=${keyword}`)
      .pipe(catchError(this.handleError));
  }

  deleteBankAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/accounts/${id}`)
      .pipe(catchError(this.handleError));
  }

  updateBankAccount(account: BankAccount): Observable<BankAccount> {
    return this.http.put<BankAccount>(`${this.apiUrl}/accounts/${account.id}`, account)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: any): Observable<never> {
    console.error('An error occurred', error);
    return throwError(error);
  }

  createAccount(account: BankAccount):Observable<BankAccount> {
    return this.http.post<BankAccount>(`${this.apiUrl}/accounts`, account)
  }
}
