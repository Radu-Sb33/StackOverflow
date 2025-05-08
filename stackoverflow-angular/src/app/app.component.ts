import { Component, OnInit } from '@angular/core';
import { PrimeNG } from 'primeng/config';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {LoginComponent} from "./auth-comp/login/login.component";
import {SignupComponent} from "./auth-comp/signup/signup.component";
import {Menubar, MenubarModule} from 'primeng/menubar';
import {Badge} from "primeng/badge";
import {NgClass} from "@angular/common";
import {Avatar} from "primeng/avatar";
import {MenuItem} from "primeng/api";
import {Ripple} from "primeng/ripple";
import {InputText} from "primeng/inputtext";
import {UserService} from "./services/user.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LoginComponent, SignupComponent, Menubar, Badge, NgClass, Avatar, Ripple, InputText, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})


export class AppComponent implements OnInit{
  constructor(private primeng: PrimeNG, private userService: UserService) {}

  ngOnInit() {
    this.primeng.ripple.set(true);
    this.items = [
      { label: 'Home', icon: 'fa fa-home', routerLink: [''] },
      { label: 'Login', icon: 'fa-solid fa-right-to-bracket', routerLink: ['/login'] },
      { label: 'Signup', icon: 'fa-solid fa-user-plus', routerLink: ['/signup'] },
      { label: 'Questions', icon: 'fa-solid fa-question', routerLink: ['/questions'] },
      { label: 'Answers', icon: 'fa-solid fa-wand-magic-sparkles', routerLink: ['/answers']},
      { label: 'Users', icon: 'bi bi-people', routerLink: ['/users']},
      { label: 'Logout', icon: '', routerLink:[''],  command: () => {
          this.userService.logout();

        },

      }
    ];
  }
  title = 'stackoverflow-angular';
  items: MenuItem[] | undefined;


}

