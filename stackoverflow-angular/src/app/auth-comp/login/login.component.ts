import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {Checkbox} from "primeng/checkbox";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {InputText} from "primeng/inputtext";
import {StyleClassModule} from 'primeng/styleclass';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {User} from "../../models/user";
import {UserService} from "../../services/user.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    RouterLink,
    Checkbox,
    ButtonDirective,
    Ripple,
    InputText,
    StyleClassModule,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  user: Partial<User> = { email: '', password: '' };
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  loginUser() {
    if (this.loginForm.invalid) {
      // Mark all fields as touched to display validation errors
      this.loginForm.markAllAsTouched();
      return;
    }

    const { email, password } = this.loginForm.value;

    this.userService.login({ email, password }).subscribe({
      next: (response: string) => {
        console.log('Login successful:', response);
        console.log('Autentificat: ',this.userService.isAuthenticated);
        alert('You have logged in!');
        this.userService.isAuthenticated = true;
        // Store the token or user info in local storage or session storage
        const userId = this.userService.getUserIDbyEmail(email); // Extract the user ID
        this.userService.getUserRoleByEmail(email).subscribe((role: string) => {
          localStorage.setItem('userRole', role); // Setează rolul
        });

        //localStorage.setItem('userId', userId.toString());
        localStorage.setItem('userId', userId.toString());
        localStorage.setItem('emailLogged', email);
        localStorage.setItem('authToken', response);

        // Redirect to the desired route after successful login
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Login failed:', err);

        if (err.status === 403 || err.error?.message?.includes('banned')) {
          alert('You have been banned from the platform.');
        } else {
          alert('Invalid credentials. Please try again.');
        }
      }

    });
  }


}
