import {Component, OnInit} from '@angular/core';
import {UserService} from "../services/user.service";
import {User} from "../models/user";
import {RouterLink} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {DatePipe, formatDate, NgForOf, NgIf, NgStyle} from "@angular/common";
import {TableModule} from "primeng/table";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {Tag} from "primeng/tag";

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    RouterLink,
    NgForOf,
    TableModule,
    ButtonDirective,
    Ripple,
    Tag,
    NgStyle,
    DatePipe,
    NgIf
  ],
  templateUrl: './users.component.html',
  styleUrl: './users.component.scss'
})

export class UsersComponent implements OnInit{
  // user: User = { email: '', password: '', confirm_password: '' };
  // message: string = '';
  public userRole: string = 'moderator'; // Rolul utilizatorului curent (înlocuiește cu logica reală de autentificare)



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
  // register() {
  //   this.userService.register(this.user).subscribe({
  //     next: (response) => {
  //       this.message = 'User registered successfully!';
  //     },
  //     error: (err) => {
  //       this.message = 'Error registering user!';
  //     },
  //   });
  // }


  // public banUser(user: ) {
  //
  // }
}
