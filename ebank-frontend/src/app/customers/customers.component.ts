import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Customer} from "../models/customer";
import {catchError, Observable} from "rxjs";
import {CustomerService} from "../services/customer.service";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.getCustomers();
  }

  getCustomers(): void {
    this.customerService.getCustomers()
      .subscribe(
        data => {
          this.customers = data;
        },
        error => {
          console.error('Error fetching customers', error);
        }
      );
  }
}
