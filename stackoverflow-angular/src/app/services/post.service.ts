import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../models/user";
import {Question} from "../models/question";
import {PostType} from "../models/postType";

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private baseUrl = 'http://localhost:8080/post'; // Adjust the URL if needed

  constructor(private http: HttpClient) {}

  getQuestions(): Observable<Question[]> {
    return this.http.get<any[]>(`${this.baseUrl}/questions`);
  }

  getByID(id: number | null): Observable<Question> {
    return this.http.get<any>(`${this.baseUrl}/getPostById/${id}`);
  }

  getType(id: number): Observable<PostType>{
    return this.http.get<PostType>(`${this.baseUrl}/getType/${id}`)
  }

  getAnswersForQuestion(questionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/questions/${questionId}/answers`);
  }

  createPost(question: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/createPost`, question);
  }
}
