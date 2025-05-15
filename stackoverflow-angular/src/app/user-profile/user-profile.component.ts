import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { Subscription } from 'rxjs';
import { UserService } from '../services/user.service'; // Ajustează calea dacă e necesar
import { User } from '../models/user'; // Ajustează calea dacă e necesar
import { Router } from '@angular/router';
import { Timestamp } from 'rxjs'; // Import necesar pentru a referenția tipul corect

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'] // *** ASIGURĂ-TE CĂ ACEASTĂ LINIE EXISTĂ ȘI ESTE CORECTĂ ***
})
export class UserProfileComponent implements OnInit, OnDestroy {
  user: User | null = null;
  displayableUser: (Omit<User, 'creation_date'> & { creation_date_for_display: Date }) | null = null;
  isLoading: boolean = true;
  errorMessage: string | null = null;
  private userSubscription: Subscription | undefined;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.userService.isAuthenticated) {
      this.router.navigate(['/login']);
      return;
    }

    const userEmail = localStorage.getItem('emailLogged');
    if (userEmail) {
      this.isLoading = true;
      this.userSubscription = this.userService.getUserByEmail(userEmail).subscribe({
        next: (userData) => {
          this.user = userData;

          if (this.user) {
            const { creation_date, ...otherProps } = this.user;
            this.displayableUser = {
              ...otherProps,
              creation_date_for_display: creation_date // direct ca Date
            };
          }

          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error fetching user profile data:', err);
          this.errorMessage = 'Could not load profile data. Please try again later.';
          this.isLoading = false;
        }
      });
    } else {
      console.error('User email not found in localStorage.');
      this.errorMessage = 'An error occurred while identifying the user.';
      this.isLoading = false;
      this.router.navigate(['/login']);
    }
  }


  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }
}
