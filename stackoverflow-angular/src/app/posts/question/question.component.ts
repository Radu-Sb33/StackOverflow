import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {NgForOf, NgIf} from "@angular/common";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import { PostService } from '../../services/post.service';
@Component({
  selector: 'app-question',
  standalone: true,
  imports: [
    NgIf,
    ButtonDirective,
    Ripple,
    NgForOf
  ],
  templateUrl: './question.component.html',
  styleUrl: './question.component.scss'
})
export class QuestionComponent implements OnInit{
  questions: any[] = [];
  isAuthenticated = false;
  constructor(private userService: UserService, private postService: PostService) {
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
    console.log("Fac intrebari");
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
