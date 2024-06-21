import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomerService } from '../services/customer.service';
import { Customer } from '../models/customer';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html',
  styleUrls: ['./new-customer.component.css']
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup!: FormGroup;

  constructor(private fb: FormBuilder, private customerService: CustomerService, private router: Router) {}

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      birthday: [null, [Validators.required]],
      postalAddress: [null, [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.newCustomerFormGroup.valid) {
      const newCustomer: Customer = this.newCustomerFormGroup.value;
      console.log('New Customer:', newCustomer);
      this.customerService.saveCustomer(newCustomer).subscribe({
        next: data => {
          Swal.fire({
            icon: 'success',
            title: 'Success!',
            text: 'Customer has been successfully saved!',
          }).then(() => {
            this.router.navigateByUrl('/customers'); // Redirection vers une autre page après la sauvegarde
          });
        },
        error: err => {
          console.error('Error saving customer:', err);
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Failed to save customer. Please try again.',
          });
        }
      });
    } else {
      console.log('Form is invalid');
    }
  }
}
