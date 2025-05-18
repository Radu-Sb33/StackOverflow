import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { User } from "../models/user";
import { Question } from "../models/question";
import { PostType } from "../models/postType";
import { Tag } from '../models/tag';
import {PostTag} from "../models/PostTag";
import {Answer} from "../models/answer";

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private baseUrl = 'http://localhost:8080/post';
  private generalApiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getQuestions(): Observable<Question[]> {
    return this.http.get<Question[]>(`${this.baseUrl}/questions`);
  }

  getByID(id: number | null): Observable<Question> {
    return this.http.get<Question>(`${this.baseUrl}/getPostById/${id}`);
  }

  getAnswerByID(id: number | null): Observable<Answer> {
    return this.http.get<Answer>(`${this.baseUrl}/getPostById/${id}`);
  }

  getType(id: number): Observable<PostType>{
    // Asigură-te că acest URL este corect (ex: /post-types/{id} sau /post/types/{id})
    return this.http.get<PostType>(`${this.generalApiUrl}/post/getType/${id}`)
      .pipe(catchError(this.handleError));
  }

  getAnswersForQuestion(questionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/questions/${questionId}/answers`);
  }

  createPost(question: Partial<Question>): Observable<Question> {
    return this.http.post<Question>(`${this.baseUrl}/createPost`, question)
      .pipe(catchError(this.handleError));
  }

  createPostTag(postTagPayload: PostTag): Observable<PostTag> {
    console.log("Payload:", postTagPayload);
    return this.http.post<PostTag>(`${this.generalApiUrl}/postTag/createPostTag`, postTagPayload)
      .pipe(catchError(this.handleError));

  }

  private handleError(error: HttpErrorResponse) {
    let displayMessage = 'A apărut o eroare necunoscută. Vă rugăm încercați din nou.';

    if (error.error instanceof ErrorEvent) {
      // Eroare client-side sau de rețea
      console.error('Eroare client/rețea:', error.error.message);
      displayMessage = `Eroare de rețea sau client: ${error.error.message || 'necunoscută'}`;
    } else {

      // Eroare de la backend
      console.error(
        `Backend a returnat cod ${error.status || 'necunoscut'}, ` +
        `corpul erorii: ${JSON.stringify(error.error)}`, error
      );

      if (error.status === 0) {
        displayMessage = 'Serviciul nu este momentan disponibil. Verificați conexiunea la internet și încercați din nou.';
      } else if (error.error && typeof error.error.message === 'string' && error.error.message.trim() !== '') {
        displayMessage = error.error.message;
      } else if (error.error && typeof error.error === 'string' && error.error.trim() !== '') {
        displayMessage = error.error;
      } else if (error.statusText && error.status !== 0) { // error.message este statusText pentru HttpErrorResponse
        displayMessage = `Eroare ${error.status}: ${error.statusText}`;
      } else if (error.status !== 0) {
        displayMessage = `Eroare ${error.status} de la server.`;
      }
    }
    // Returnează un Observable cu un mesaj de eroare user-facing
    return throwError(() => new Error(displayMessage));
  }
}
