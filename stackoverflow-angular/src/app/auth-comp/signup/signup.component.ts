import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../models/user";
import {RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Checkbox} from "primeng/checkbox";
import {InputText} from "primeng/inputtext";
import {Ripple} from "primeng/ripple";
import {FormsModule} from "@angular/forms";
import {map, Observable} from "rxjs";

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    ButtonDirective,
    Checkbox,
    InputText,
    Ripple,
    FormsModule,
    NgClass,
    NgIf
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent implements OnInit {
  user: Partial<User> = {email: '', username: '', password: ''};
  message: string = '';
  confirmPassword: string = '';
  registrationSuccess: boolean = false;


  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.getUsers();
  }

  public users: User[] = [];

  public getUsers(): void {
    this.userService.getUsers().subscribe(
      (response: User[]) => {
        this.users = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }


  register() {
    const expression: RegExp = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/i;
    const userEmail: string | undefined = this.user.email;

    if (!expression.test(<string>userEmail)) {
      this.registrationSuccess = false;
      this.message = 'Invalid email format. Please enter a valid email address.';
    } else if (this.user.password !== this.confirmPassword) {
      this.registrationSuccess = false;
      this.message = 'Passwords do not match. Please confirm your password correctly.';
    } else {
      this.userService.checkEmailExists(this.user.email!).subscribe({
        next: (emailExists: boolean) => {
          if (emailExists) {
            this.registrationSuccess = false;
            this.message = 'This email is already registered. Please use a different email.';
          } else {
            // Check username now
            this.userService.checkUsernameExists(this.user.username!).subscribe({
              next: (usernameExists: boolean) => {
                if (usernameExists) {
                  this.registrationSuccess = false;
                  this.message = 'This username is already taken. Please choose another one.';
                } else {
                  // Everything OK, proceed to register
                  this.userService.register(this.user).subscribe({
                    next: () => {
                      this.registrationSuccess = true;
                      this.message = 'Registration successful! You can now log in.';
                    },
                    error: () => {
                      this.registrationSuccess = false;
                      this.message = 'An error occurred during registration. Please try again.';
                    }
                  });
                }
              },
              error: () => {
                this.registrationSuccess = false;
                this.message = 'Unable to verify username availability.';
              }
            });
          }
        },
        error: () => {
          this.registrationSuccess = false;
          this.message = 'Unable to verify email availability.';
        }
      });
    }
  }

}
