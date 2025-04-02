import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {catchError, map, Observable, of} from 'rxjs';
import { User } from '../models/user';
import {UsersComponent} from "../users/users.component";

@Injectable({
  providedIn: 'root',
})
export class UserService {
  isAuthenticated = false;
  private baseUrl = 'http://localhost:8080/user'; // Adjust the backend URL if needed

  constructor(private http: HttpClient) {}

  public getUsers(): Observable<User[]>{
    return this.http.get<User[]>(`${this.baseUrl}/getAllUsers`);
  }

  public register(user: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/createUser`, user);
  }

  login(user: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, user);
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.baseUrl}/updateUserById/${user.id}`, user);
  }

  public deleteUser(userID: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/deleteById/${userID}`);
  }

  public checkEmailExists(email: string | undefined): Observable<boolean> {
    if (!email) {
      return of(false);
    }

    return this.http.get<User[]>(`${this.baseUrl}/get/byEmail/${email}`).pipe(
      map((users) => users.length > 0), // Check if any users exist with the given email
      catchError(() => of(false))
    );
  }

}
