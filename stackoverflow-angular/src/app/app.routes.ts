import { Routes } from '@angular/router';
import {LoginComponent} from "./auth-comp/login/login.component";
import {SignupComponent} from "./auth-comp/signup/signup.component";

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
];
