import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {User} from "../models/user";
import {Question} from "../models/question";
import {PostType} from "../models/postType";
import {Tag} from "../models/tag";

@Injectable({
  providedIn: 'root',
})
export class TagService {
  private baseUrl = 'http://localhost:8080/tag'; // Adjust the URL if needed

  constructor(private http: HttpClient) {
  }

  getTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${this.baseUrl}/getAllTags`);
  }

  createTag(tag: Partial<Tag>): Observable<Tag> { // Specificăm tipul pentru payload și răspuns
    return this.http.post<Tag>(`${this.baseUrl}/createTag`, tag);
  }
}
