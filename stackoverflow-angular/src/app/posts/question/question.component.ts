import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {DatePipe, NgForOf, NgIf, NgStyle} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import { PostService } from '../../services/post.service';
import {Router} from "@angular/router";
import {Answer} from "../../models/answer";
import {TagService} from "../../services/tag.service";
import { VoteService, VotePayload, VoteResponseDTO } from "../../services/vote.service"
import {PostTag} from "../../models/PostTag";
import {Observable, of} from 'rxjs';
import { map, catchError } from 'rxjs/operators';

const VOTE_TYPE_LIKE = 1;
const VOTE_TYPE_DISLIKE = 2;
interface DisplayTag {
  tagName: string;
  color: string;
}

@Component({
  selector: 'app-question',
  standalone: true,
  imports: [
    NgIf,
    ButtonDirective,
    Ripple,
    NgForOf,
    DatePipe,
    NgStyle
  ],
  templateUrl: './question.component.html',
  styleUrl: './question.component.scss'
})



export class QuestionComponent implements OnInit{
  isVoting = false;
  questions: any[] = [];
  isAuthenticated = false;
  currentUserId: number|undefined;
  constructor(private userService: UserService, private postService: PostService, private router: Router, private voteService: VoteService) {
  }

  ngOnInit() {
    this.isAuthenticated = this.userService.isAuthenticated;
    const currentUserIdString = localStorage.getItem("userId");
    if(currentUserIdString!=null){ this.currentUserId = parseInt(currentUserIdString,10);}

    this.postService.getQuestions().subscribe({
      next: (questions) => {
        this.questions = questions;

        this.questions.forEach(question => {
          // 1) Tags
          if (question.id != null) {
            this.loadTags(question.id);
          } else {
            console.warn('Întrebare fără ID:', question);
          }

          // 2) Username autor întrebare
          const creator = question.createdByUser;
          if (creator) {
            if (typeof creator === 'number') {
              this.userService.getUsernameById(creator)
                .subscribe({
                  next: name => question.createdByUsername = name,
                  error: () => question.createdByUsername = 'Unknown'
                });
            } else if (creator.username) {
              question.createdByUsername = creator.username;
            } else {
              question.createdByUsername = 'Unknown';
            }
          } else {
            question.createdByUsername = 'Unknown';
          }

          // 3) Răspunsuri
          if (question.id != null) {
            this.postService.getAnswersForQuestion(question.id)
              .subscribe({
                next: (answers: Answer[]) => {
                  // rezolvă username-ul fiecărui answer
                  answers.forEach(ans => {
                    const u = ans.createdByUser;
                    if (typeof u === 'number') {
                      this.userService.getUsernameById(u)
                        .subscribe({
                          next: name => ans.createdByUsername = name,
                          error: () => ans.createdByUsername = 'Unknown'
                        });
                    } else if (u && u.username) {
                      ans.createdByUsername = u.username;
                    } else {
                      ans.createdByUsername = 'Unknown';
                    }
                  });
                  // sort desc
                  question.answers = answers.sort(
                    (a, b) => new Date(b.postedDate).getTime() - new Date(a.postedDate).getTime()
                  );
                },
                error: err => {
                  console.error(`Eroare la încărcarea răspunsurilor pentru ${question.id}:`, err);
                  question.answers = [];
                }
              });
          }

          // 4) Scor (up – down)
          if (question.id != null) {
            this.voteService.getScoreForQuestion(question.id)
              .subscribe({
                next: score => question.voteScore = score,
                error: () => question.voteScore = 0
              });
          }
        });

        // 5) Sortare globală întrebări
        this.questions.sort(
          (a, b) => new Date(b.postedDate).getTime() - new Date(a.postedDate).getTime()
        );
      },
      error: err => {
        console.error('Eroare la încărcarea întrebărilor:', err);
      }
    });
  }


  navigateToAddQuestion() {
    this.router.navigate(['/add-question']);
  }
  navigateToAddAnswer(questionId: number) {
    localStorage.setItem('idParinte', questionId.toString());
    this.router.navigate(['/add-answer']);
  }
  navigateToEditQuestion(questionId: number) {
    //apelez metoda(id)
    this.router.navigate(['/edit-question',questionId]);
  }
  navigateToEditAnswer(answerId: number) {
    //apelez metoda(id)
    this.router.navigate(['/edit-answer',answerId]);
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    return '#' + Array.from({ length: 6 })
      .map(() => letters[Math.floor(Math.random() * 16)])
      .join('');
  }


  //tagMap: { [postId: number]: string[] } = {};
  tagMap: Record<number, { tagName: string; color: string }[]> = {};


  loadTags(postId: number): void {
    this.postService.getPostTagsByPostId(postId).pipe(
      map((postTags: PostTag[]) => { // The incoming data should now conform to PostTag[]
        return postTags
          .filter(pt => pt && pt?.tag && pt?.tag?.tagName) // Keep this defensive filter
          .map(pt => ({
            tagName: pt.tag.tagName,
            color: this.getRandomColor()
          }));
      }),
      catchError((err) => {
        console.error('Failed to load tags for post:', err);
        this.tagMap[postId] = [];
        return of([]);
      })
    ).subscribe({
      next: (displayTags: DisplayTag[]) => {
        this.tagMap[postId] = displayTags;
        console.log(`Taguri pentru post ${postId}:`, this.tagMap[postId]);
      },
      error: (err) => {
        console.error('Final error in loadTags subscription:', err);
      }
    });
  }


  public castVote(postId: number, desiredVoteType: number): void {
    if (this.isVoting) {
      return; // Prevent multiple clicks while processing
    }

    const email = localStorage.getItem("emailLogged");
    if (!email) {
      console.error("Email-ul nu a fost găsit în localStorage.");
      alert("Trebuie să fii autentificat pentru a vota.");
      return;
    }

    this.isVoting = true; // Set voting flag at the beginning of the operation

    this.userService.getUserIDbyEmail(email).subscribe({
      next: (res) => {
        const currentUserId = res.userId;
        if (!currentUserId) {
          console.error('Utilizatorul nu este autentificat.');
          alert('Trebuie să fii autentificat pentru a vota.');
          this.isVoting = false;
          return;
        }

        this.processVote(currentUserId, postId, desiredVoteType);
      },
      error: (err) => {
        this.isVoting = false;
        console.error('Eroare la obținerea user ID-ului:', err);
        alert("A apărut o eroare la autentificare.");
      }
    });
  }

  private processVote(currentUserId: number, postId: number, desiredVoteType: number): void {
    this.voteService.getVoteByUserAndPost(currentUserId, postId).subscribe({
      next: (existingVote: VoteResponseDTO | null) => {
        if (existingVote) {
          // User has an existing vote
          if (existingVote.voteType === desiredVoteType) {
            // User already has the desired vote type, do nothing
            console.log(`User already has vote type ${desiredVoteType}. No action taken.`);
            this.isVoting = false;
            return;
          } else {
            // User has a different vote type, or wants to change their vote
            console.log(`User had vote type ${existingVote.voteType}. Changing to ${desiredVoteType}.`);
            this.voteService.deleteVote(existingVote.id).subscribe({
              next: () => {
                console.log('Previous vote deleted.');
                this.createVote(currentUserId, postId, desiredVoteType);
              },
              error: (delError) => {
                this.isVoting = false;
                console.error('Eroare la ștergerea votului anterior:', delError);
                alert(`A apărut o eroare la schimbarea votului: ${delError.error?.message || delError.message || 'Vă rugăm încercați din nou.'}`);
              }
            });
          }
        } else {
          // No existing vote, create a new one
          console.log(`No existing vote. Creating new vote of type ${desiredVoteType}.`);
          this.createVote(currentUserId, postId, desiredVoteType);
        }
      },
      error: (fetchVoteError) => {
        this.isVoting = false;
        console.error('Eroare la verificarea votului existent:', fetchVoteError);
        alert(`A apărut o eroare la verificarea votului: ${fetchVoteError.error?.message || fetchVoteError.message || 'Vă rugăm încercați din nou.'}`);
      }
    });
  }

  private createVote(currentUserId: number, postId: number, voteType: number): void {
    const payload: VotePayload = {
      voteType: { id: voteType }, // Ensure backend expects this object structure for creation
      votedByUser: { id: currentUserId },
      post: { id: postId }
    };

    this.voteService.createVote(payload).subscribe({
      next: (newlyCreatedVote: VoteResponseDTO) => {
        console.log(`Vote registered successfully for type ${voteType}:`, newlyCreatedVote);
        this.updateScoreForPost(postId);
        this.isVoting = false;
      },
      error: (error) => {
        this.isVoting = false;
        console.error(`Eroare la înregistrarea votului de tip ${voteType}:`, error);
        alert(`A apărut o eroare la vot: ${error.error?.message || error.message || 'Vă rugăm încercați din nou.'}`);
      }
    });
  }

// Your public methods to call the generic castVote method
  public castVoteThumbsUp(postId: number): void {
    this.castVote(postId, VOTE_TYPE_LIKE);
  }

  public castVoteThumbsDown(postId: number): void {
    this.castVote(postId, VOTE_TYPE_DISLIKE);
  }

  private updateScoreForPost(postId: number): void {
    this.voteService.getScoreForQuestion(postId).subscribe({
      next: (score) => {
        const question = this.questions.find(q => q.id === postId);
        if (question) {
          question.voteScore = score;
        }
        this.isVoting = false;
      },
      error: (err) => {
        console.error('Eroare la actualizarea scorului:', err);
        this.isVoting = false;
      }
    });
  }

  // logDebugInfo(prefix: string, itemId: any, isAuthenticated: boolean, currentUserId: any, creatorObj: any): boolean {
  //   const creatorId = creatorObj?.id;
  //   const condition = isAuthenticated && currentUserId === creatorId;
  //   console.log(`${prefix} - Item ID: ${itemId}, IsAuth: ${isAuthenticated}, CurrUserID: ${currentUserId}, CreatorID: ${creatorId}, ConditionMet: ${condition}`);
  //
  //   // Puteți returna orice valoare booleană aici, de obicei true,
  //   // pentru a nu interfera cu randarea dacă o plasați într-un context care așteaptă o valoare.
  //   // Sau, dacă o folosiți doar pentru efectul secundar de logging, o puteți lăsa fără return specific în template,
  //   // dar e mai sigur să returnați ceva dacă o intercalați într-un mod neașteptat.
  //   // Totuși, cel mai bine este să o apelați într-un context unde valoarea de return nu contează, de ex. {{ logDebugInfo(...) }}
  //   return true;
  // }
}
