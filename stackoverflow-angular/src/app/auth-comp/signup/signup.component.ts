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
import {Observable} from "rxjs";

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
export class SignupComponent implements OnInit{
  user: Partial<User> = { email: '',username: '', password: '' };
  message: string = '';
  confirmPassword: string = '';
  registrationSuccess: boolean = false;


  constructor(private userService: UserService) {}

  ngOnInit() {
    this.getUsers();
  }

  public users: User[]=[];

  public getUsers(): void{
    this.userService.getUsers().subscribe(
      (response: User[]) =>{
        this.users = response;
      },
      (error: HttpErrorResponse) =>{
        alert(error.message);
      }
    );
  }
  register() {
    const expression: RegExp = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/i;
    const userEmail: string | undefined = this.user.email;
    if (!expression.test(<string>userEmail)){
      this.registrationSuccess = false;
      this.message = 'Incorrect Email Introduction!';
    }
    else if (this.user.password !== this.confirmPassword) {
      this.registrationSuccess = false;
      this.message = 'Passwords do not match!';
    } else {
      this.userService.register(this.user).subscribe({
        next: (response) => {
          this.registrationSuccess = true;
          this.message = 'User registered successfully!';
        },
      });
    }
  }
}
