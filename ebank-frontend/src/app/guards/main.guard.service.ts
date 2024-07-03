// import { Injectable } from '@angular/core';
// import { CanActivate, CanLoad, Router } from '@angular/router';
// import { AuthService } from '../services/auth.service';
//
// @Injectable({
//   providedIn: 'root'
// })
// export class MainGuard implements CanActivate, CanLoad {
//
//   constructor(private authService: AuthService, private router: Router) {}
//
//   canActivate(): boolean {
//     return this.canLoad();
//   }
//
//   canLoad(): boolean {
//     if (!this.authService.isLoggedIn()) {
//       this.router.navigate(['/login']); // Redirige vers la page de connexion si non connecté
//       return false;
//     }
//     return true;
//   }
// }
