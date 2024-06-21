import { Component, OnInit } from '@angular/core';
import { Customer } from '../models/customer';
import { CustomerService } from '../services/customer.service';
import { map, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { FormBuilder, FormGroup } from "@angular/forms";
import Swal from 'sweetalert2';
import {Router} from "@angular/router";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customers$!: Observable<Customer[]>;
  errorMessage!: string;
  searchFormGroup: FormGroup;

  constructor(private customerService: CustomerService, private fb: FormBuilder, private router: Router) {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control("")
    });
  }

  ngOnInit(): void {
    this.loadCustomers();
    this.handleSearchCustomer();
  }

  loadCustomers(): void {
    this.customers$ = this.customerService.getCustomers()
      .pipe(
        catchError(error => {
          this.errorMessage = 'Error fetching customers';
          console.error('Error fetching customers', error);
          return throwError(error);
        })
      );
  }

  handleSearchCustomer(): void {
    const kw = this.searchFormGroup.value.keyword;
    this.customers$ = this.customerService.searchCustomers(kw)
      .pipe(
        catchError(err => {
          this.errorMessage = err.message;
          console.error('Error searching customers', err);
          return throwError(err);
        })
      );
  }

  handleDeleteButton(c: Customer): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this customer!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.isConfirmed) {
        this.customerService.deleteCustomer(c.id).subscribe({
          next: () => {
            Swal.fire(
              'Deleted!',
              'Your customer has been deleted.',
              'success'
            ).then(() => {
              // Reload customers after deletion
              this.loadCustomers();
            });
          },
          error: err => {
            console.error('Error deleting customer', err);
            Swal.fire(
              'Error!',
              'Failed to delete customer. Please try again later.',
              'error'
            );
          }
        });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        Swal.fire(
          'Cancelled',
          'Your customer is safe :)',
          'error'
        );
      }
    });
  }

  goToUpdateCustomer(c: Customer): void {
    this.customerService.updateCustomer(c).subscribe({
      next: updatedCustomer => {
        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: 'Customer has been updated successfully.',
          showConfirmButton: false,
          timer: 1500
        }).then(() => {
          // Naviguer vers une autre page ou actualiser la liste de clients après la mise à jour
          this.loadCustomers();
        });
      },
      error: err => {
        console.error('Error updating customer', err);
        Swal.fire({
          icon: 'error',
          title: 'Error!',
          text: 'Failed to update customer. Please try again later.',
          showConfirmButton: false,
          timer: 1500
        });
      }
    });
  }

}
