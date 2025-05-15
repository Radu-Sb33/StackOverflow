import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import { User } from '../models/user';
import {UsersComponent} from "../users/users.component";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root',
})
export class UserService {
  public isAuthenticated = false;
  private baseUrl = 'http://localhost:8080/user'; // Adjust the backend URL if needed
  //public emailLogged: string | undefined;

  constructor(private http: HttpClient, private router: Router) {
    //this.checkAuthentication();
  }

  private checkAuthentication(): void {
    const token = localStorage.getItem('authToken');
    this.isAuthenticated = !!token; // Set isAuthenticated based on token presence
  }

  public logout(): void {
    if(localStorage.getItem('authToken')!=null){alert('Logged Out Successfully!');}

    // 1. Șterge token-ul din localStorage
    localStorage.removeItem('authToken'); // Folosește aceeași cheie ca la login
    localStorage.removeItem('emailLogged');// Apelează funcția de logout din service
    localStorage.removeItem('idParinte'); // Folosește aceeași cheie ca la login
    localStorage.removeItem('userRole');
    localStorage.removeItem('userId');
    // 2. Actualizează starea de autentificare
    this.isAuthenticated = false;



    this.router.navigate(['/login']);
  }

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.baseUrl}/getAllUsers`);
  }

  public getUserByEmail(email: string): Observable<User>{
    return this.http.get<User>(`${this.baseUrl}/get/byEmail/${email}`)
  }

  public getUserIDbyEmail(email: string): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/getUserIdByEmail`);
  }

  public register(user: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/createUser`, user);
  }

  login(user: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, user).pipe(
      map((response: any) => {

        if (response && response.token) {
          localStorage.setItem('authToken', response.token); // Salvează token-ul
          this.isAuthenticated = true; // Setează starea ca autentificat
        }
        return response;
      }),
      catchError((error) => {
        this.isAuthenticated = false; // Asigură-te că starea este falsă la eroare
        throw error; // Relansează eroarea pentru a fi gestionată de componentă
      })
    );
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/updateUserById/${user.id}`, user);
  }

  public deleteUser(userID: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/deleteById/${userID}`);
  }

  checkEmailExists(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-email?email=${email}`);
  }

  checkUsernameExists(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-username?username=${username}`);
  }

  getUserRoleByEmail(email: string): Observable<string> {
    return this.http.get<User>(`${this.baseUrl}/get/byEmail/${email}`)
      .pipe(map(user => user.is_moderator ? 'moderator' : 'user'));
  }

  banUser(userId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/ban-user/${userId}`, {});
  }

  unbanUser(userId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/unban-user/${userId}`, {});
  }







}
