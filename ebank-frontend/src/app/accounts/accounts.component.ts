import {Component, OnInit, signal} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import {AccountStatus, BankAccount} from '../models/bank-account';
import { BankAccountService } from '../services/bank-account.service';
import { Router } from '@angular/router';
import {Customer} from "../models/customer";
import {CustomerService} from "../services/customer.service";

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html',
  styleUrls: ['./accounts.component.css']
})
export class AccountsComponent implements OnInit {

  bankAccounts$!: Observable<BankAccount[]>;
  errorMessage!: string;
  searchFormGroup: FormGroup;
  addAccountFormGroup!: FormGroup;
  customers: Customer[] = [];
  page: number = 0;
  size: number = 8;

  constructor(
    private bankAccountService: BankAccountService,
    private customerService: CustomerService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.searchFormGroup = this.fb.group({
      keyword: ['']
    });
    this.addAccountFormGroup=this.fb.group({
      rib:['',Validators.required],
      balance:['', Validators.required],
      customerId: ['', Validators.required]
    })
  }

  ngOnInit(): void {
    this.loadBankAccounts();
    this.handleSearchBankAccount();
    this.loadCustomers();
  }
  loadCustomers(): void {
    this.customerService.getCustomers().subscribe({
      next: (customers) => {
        this.customers = customers;
      },
      error: (err) => {
        console.error('Error loading customers', err);
      }
    });
  }

  loadBankAccounts(): void {
    this.bankAccounts$ = this.bankAccountService.getAccounts(this.page,this.size)
      .pipe(
        catchError(error => {
          this.errorMessage = 'Error fetching bank accounts';
          console.error('Error fetching bank accounts', error);
          return throwError(error);
        })
      );
  }

  handleSearchBankAccount(): void {
    const kw = this.searchFormGroup.value.keyword;
    this.bankAccounts$ = this.bankAccountService.searchAccounts(kw, this.page,this.size)
      .pipe(
        catchError(err => {
          this.errorMessage = err.message;
          console.error('Error searching bank accounts', err);
          return throwError(err);
        })
      );
  }

  handleDeleteButton(account: BankAccount): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You will not be able to recover this bank account!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, keep it'
    }).then((result) => {
      if (result.isConfirmed) {
        this.bankAccountService.deleteBankAccount(account.id).subscribe({
          next: () => {
            Swal.fire(
              'Deleted!',
              'Your bank account has been deleted.',
              'success'
            ).then(() => {
              this.loadBankAccounts();
            });
          },
          error: (err: any) => {
            console.error('Error deleting bank account', err);
            Swal.fire(
              'Error!',
              'Failed to delete bank account. Please try again later.',
              'error'
            );
          }
        });
      } else if (result.dismiss === Swal.DismissReason.cancel) {
        Swal.fire(
          'Cancelled',
          'Your bank account is safe :)',
          'error'
        );
      }
    });
  }

  goToUpdateBankAccount(account: BankAccount): void {
    this.bankAccountService.updateBankAccount(account).subscribe({
      next: () => {
        Swal.fire(
          'Updated!',
          'Bank account details updated successfully.',
          'success'
        ).then(() => {
          this.loadBankAccounts(); // Recharger les comptes après la mise à jour
          // Exemple de navigation vers une autre route après la mise à jour
          this.router.navigateByUrl('/accounts');
        });
      },
      error: (err: any) => {
        console.error('Error updating bank account', err);
        Swal.fire(
          'Error!',
          'Failed to update bank account. Please try again later.',
          'error'
        );
      }
    });
  }

  // Méthode pour gérer l'ajout d'un compte bancaire
  handleAddBankAccount(): void {
    if (this.addAccountFormGroup.invalid) {
      this.addAccountFormGroup.markAllAsTouched();
      return;
    }

    const newAccount: BankAccount = {
      id: 0,
      rib: this.addAccountFormGroup.value.rib,
      balance: this.addAccountFormGroup.value.balance,
      status: AccountStatus.OPEN,
      customerId: this.addAccountFormGroup.value.customerId  // Remplacez par l'ID réel du client si nécessaire
    };

    this.bankAccountService.createAccount(newAccount).subscribe({
      next: (account) => {
        Swal.fire({
          title: 'Success!',
          text: 'Bank account added successfully.',
          icon: 'success',
          confirmButtonText: 'OK'
        }).then(() => {
          this.loadBankAccounts();
          this.addAccountFormGroup.reset();
        });
      },
      error: (err) => {
        console.error('Error adding bank account', err);
        Swal.fire({
          title: 'Error!',
          text: 'Failed to add bank account. Please try again later.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    });
}
}
