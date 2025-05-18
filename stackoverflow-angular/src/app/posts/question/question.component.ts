import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import { PostService } from '../../services/post.service';
import {Router} from "@angular/router";
import {Answer} from "../../models/answer";
@Component({
  selector: 'app-question',
  standalone: true,
  imports: [
    NgIf,
    ButtonDirective,
    Ripple,
    NgForOf,
    DatePipe
  ],
  templateUrl: './question.component.html',
  styleUrl: './question.component.scss'
})
export class QuestionComponent implements OnInit{
  questions: any[] = [];
  isAuthenticated = false;
  constructor(private userService: UserService, private postService: PostService, private router: Router) {
  }

  ngOnInit() {
    this.isAuthenticated = this.userService.isAuthenticated;

    this.postService.getQuestions().subscribe({
      next: (data) => {
        this.questions = data;

        this.questions.forEach(question => {
          const creator = question.createdByUser;

          // --- Nume utilizator întrebare ---
          if (creator) {
            if (typeof creator === 'number') {
              this.userService.getUsernameById(creator).subscribe({
                next: (username) => question.createdByUsername = username,
                error: () => question.createdByUsername = 'Unknown'
              });
            } else if (typeof creator === 'object' && creator.username) {
              question.createdByUsername = creator.username;
            } else {
              question.createdByUsername = 'Unknown';
            }
          } else {
            question.createdByUsername = 'Unknown';
          }

          // --- Înlocuire ID-uri din answers cu obiecte Answer ---
          if (question.answers && Array.isArray(question.answers)) {
            const answerIds = question.answers as number[];
            question.answers = [];

            answerIds.forEach((answerId: number) => {
              this.postService.getAnswerByID(answerId).subscribe({
                next: (answer: Answer) => {
                  const answerCreator = answer.createdByUser;

                  if (answerCreator) {
                    if (typeof answerCreator === 'number') {
                      this.userService.getUsernameById(answerCreator).subscribe({
                        next: (username) => answer.createdByUsername = username,
                        error: () => answer.createdByUsername = 'Unknown'
                      });
                    } else if (typeof answerCreator === 'object' && answerCreator.username) {
                      answer.createdByUsername = answerCreator.username;
                    } else {
                      answer.createdByUsername = 'Unknown';
                    }
                  } else {
                    answer.createdByUsername = 'Unknown';
                  }

                  question.answers.push(answer);

                  // Sortare descrescătoare după dată a răspunsurilor
                  question.answers.sort((a: { postedDate: string | number | Date; }, b: { postedDate: string | number | Date; }) => new Date(b.postedDate).getTime() - new Date(a.postedDate).getTime());
                },
                error: (err) => {
                  console.error(`Eroare la fetch pentru răspuns ID ${answerId}:`, err);
                }
              });
            });
          }
        });

        // --- Sortare descrescătoare a întrebărilor ---
        this.questions.sort((a, b) => new Date(b.postedDate).getTime() - new Date(a.postedDate).getTime());
      },
      error: (err) => {
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

  loadAnswers(questionId: number): void {
    this.postService.getAnswersForQuestion(questionId).subscribe({
      next: (answers) => {
        const question = this.questions.find((q) => q.id === questionId);
        if (question) {
          question.answers = answers; // Adaugă răspunsurile la întrebare
        }
      },
      error: (err) => {
        console.error('Error fetching answers:', err);
      },
    });
  }
}
