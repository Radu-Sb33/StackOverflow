import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {LoginComponent} from "./auth-comp/login/login.component";
import {SignupComponent} from "./auth-comp/signup/signup.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,LoginComponent,SignupComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})

export class AppComponent {
  title = 'stackoverflow-angular';
}
