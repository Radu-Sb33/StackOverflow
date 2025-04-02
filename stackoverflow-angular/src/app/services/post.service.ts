import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private baseUrl = 'http://localhost:8080/post'; // Adjust the URL if needed

  constructor(private http: HttpClient) {}

  getQuestions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/questions`);
  }

  getAnswersForQuestion(questionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/questions/${questionId}/answers`);
  }
}
