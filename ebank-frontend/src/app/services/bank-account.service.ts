import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BankAccount } from '../models/bank-account';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BankAccountService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Récupérer tous les comptes avec pagination
  getAccounts(page: number, size: number): Observable<any> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/accounts`, { params })
      .pipe(catchError(this.handleError));
  }

  // Récupérer un compte par ID
  getAccountById(id: number): Observable<BankAccount> {
    return this.http.get<BankAccount>(`${this.apiUrl}/accounts/${id}`)
      .pipe(catchError(this.handleError));
  }

  // Récupérer un compte par RIB
  getAccountByRib(rib: string): Observable<BankAccount> {
    return this.http.get<BankAccount>(`${this.apiUrl}/accounts/rib/${rib}`)
      .pipe(catchError(this.handleError));
  }

  // Recherche de comptes par mot-clé avec pagination
  searchAccounts(keyword: string, page: number, size: number): Observable<any> {
    let params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<any>(`${this.apiUrl}/accounts/search`, { params })
      .pipe(catchError(this.handleError));
  }

  // Supprimer un compte
  deleteBankAccount(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/accounts/${id}`)
      .pipe(catchError(this.handleError));
  }

  // Mettre à jour un compte
  updateBankAccount(account: BankAccount): Observable<BankAccount> {
    return this.http.put<BankAccount>(`${this.apiUrl}/accounts/${account.id}`, account)
      .pipe(catchError(this.handleError));
  }

  // Créer un nouveau compte
  createAccount(account: BankAccount): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${this.apiUrl}/accounts`, account)
      .pipe(catchError(this.handleError));
  }

  // Gestion des erreurs
  private handleError(error: any): Observable<never> {
    console.error('An error occurred', error);
    return throwError(error);
  }
}
