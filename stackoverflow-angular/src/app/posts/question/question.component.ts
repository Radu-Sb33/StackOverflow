import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {DatePipe, NgForOf, NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import { PostService } from '../../services/post.service';
import {Router} from "@angular/router";
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
      },
      error: (err) => {
        console.error('Error fetching questions:', err);
      },
    });
  }

  navigateToAddQuestion() {
    this.router.navigate(['/add-question']);
  }
  navigateToAddAnswer() {
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
