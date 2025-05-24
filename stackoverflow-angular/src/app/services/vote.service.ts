import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

// Interfață pentru payload-ul trimis la backend pentru a crea un vot
export interface VotePayload {
  voteType: { id: number };
  votedByUser: { id: number }; // ID-ul utilizatorului curent
  post: { id: number };       // ID-ul postării (question.id)
}

// Interfață pentru răspunsul de la backend (DTO-ul discutat anterior)
// Ajustează conform cu ce returnează exact metoda ta `createVotes`
export interface VoteResponseDTO {
  id: number;
  voteType: number; // Am adăugat voteType string și votes opțional
  post: number; /* ... alte proprietăți post */
  votedByUser: number;
}




@Injectable({
  providedIn: 'root'
})
export class VoteService {
  // URL-ul către endpoint-ul backend care gestionează crearea voturilor.
  // Acesta trebuie să corespundă cu @PostMapping("/votes") sau similar din controller-ul tău Spring.
  private baseUrl = 'http://localhost:8080/vote'

  constructor(private http: HttpClient) { }

  /**
   * Trimite un nou vot către backend.
   * @param payload Datele votului.
   * @returns Observable cu răspunsul de la backend (VoteResponseDTO).
   */
  createVote(payload: VotePayload): Observable<VoteResponseDTO> {
    return this.http.post<VoteResponseDTO>(`${this.baseUrl}/createVote`, payload);
  }

  /**
   * Preia toate voturile pentru o anumită postare.
   * Folosit pentru a actualiza lista de voturi după ce un vot nou este adăugat.
   * @param postId ID-ul postării.
   * @returns Observable cu lista de VoteResponseDTO.
   */
  getVotesForPost(postId: number): Observable<VoteResponseDTO[]> {
    return this.http.get<VoteResponseDTO[]>(`${this.baseUrl}/getVotesByPostId/${postId}`);
  }

  getScoreForQuestion(postId: number): Observable<number> {
    return this.http.get<number>(`${this.baseUrl}/getScore/${postId}`);
  }

  deleteVote(voteId: number): Observable<void>{
    return this.http.delete<void>(`${this.baseUrl}/deleteVote/${voteId}`);
  }


  getVoteByUserAndPost(userId: number, postId: number): Observable<VoteResponseDTO | null> {
    return this.http.get<VoteResponseDTO>(`${this.baseUrl}/getVoteByUserAndPost/${userId}/${postId}`).pipe(
      catchError(error => {
        if (error.status === 204) { // Backend returns 204 No Content if not found
          return of(null);
        }
        throw error; // Re-throw other errors
      })
    );
  }
}
