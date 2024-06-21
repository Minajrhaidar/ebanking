import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Customer} from "../models/customer";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/agent/customers`)

  }
  searchCustomers(keyword: string): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/agent/customers/search?keyword=${keyword}`)
  }
  saveCustomer(customer:Customer): Observable<Customer> {
    return this.http.post<Customer>(`${this.apiUrl}/agent/customers`,customer)
  }
  deleteCustomer(id:number){
    return this.http.delete<Customer>(`${this.apiUrl}/agent/customers/${id}`)
  }

  updateCustomer(customer: Customer): Observable<Customer> {
    const id = customer.id;
    return this.http.put<Customer>(`${this.apiUrl}/agent/customers/${id}`, customer)
      .pipe(
        catchError(error => {
          console.error('Error updating customer', error);
          return throwError(error);
        })
      );
  }
}
