import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ModeratorGuard implements CanActivate {

  constructor(private router: Router) {}

  canActivate(): boolean {
    const role = localStorage.getItem('userRole');  // presupunem că îl setezi după login

    if (role === 'moderator') {
      return true;
    }

    alert('Access denied. Moderator role required.');
    this.router.navigate(['/home']);
    return false;
  }
}
