import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { PostService } from '../../services/post.service';
import { Router } from '@angular/router';
import {InputText} from "primeng/inputtext";
import {InputTextarea} from "primeng/inputtextarea";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {NgIf} from "@angular/common";
import {UserService} from "../../services/user.service";
import {switchMap} from "rxjs";
import {Question} from "../../models/question";
import {User} from "../../models/user";
import {HttpErrorResponse} from "@angular/common/http";
import {PostType} from "../../models/postType";

@Component({
  selector: 'app-add-question',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    InputText,
    InputTextarea,
    ButtonDirective,
    Ripple,
    NgIf
  ],
  templateUrl: './add-question.component.html',
  styleUrl: './add-question.component.scss'
})
export class AddQuestionComponent {
  addQuestionForm: FormGroup;
  title: string='';
  content: string='';
  type: PostType | undefined = undefined;
  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    public userService: UserService
  ) {
    this.addQuestionForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
      type: postService.getType(1)
    });
  }

  ngOnInit(): void {
    // Fetch the PostType object with id = 1 on component initialization
    this.postService.getType(1).subscribe(
      (response: PostType) => {
        this.type = response; // Assign the fetched PostType object to `type`
      },
      (error: HttpErrorResponse) => {
        console.error('Error fetching PostType:', error.message);
        alert('Failed to load question type. Please try again.');
      }
    );
  }

  onSubmit() {
    if (this.addQuestionForm.invalid) {
      return;
    }

    const { title, content,type } = this.addQuestionForm.value;

    const email = localStorage.getItem('emailLogged');
    if (!email) {
      alert('You must be logged in to add a question.');
      this.router.navigate(['/login']);
      return;
    }
    this.userService.getUserByEmail(email).pipe(
      switchMap((user) => {
        if (!user) {
          throw new Error('User not found');
        }

        // Create the question object with the user
        const question: Partial<Question> = {
          createdByUser: user,
          postType: this.type,
          postTitleQ: title,
          postContent: content,
        };

        // Send the question to the backend
        return this.postService.createPost(question);
      })
    ).subscribe({
      next: (createdQuestion: Question) => {
        // Extract the ID of the newly created question
        const questionId = createdQuestion.id;

        // Store the ID in localStorage
        if (questionId) {
          localStorage.setItem('idParinte', questionId.toString());
          console.log('Stored question ID in localStorage:', questionId);
        }
        this.router.navigate(['/questions']);
      },
      error: (err) => {
        console.error('Error adding question:', err);
        alert(err.message || 'Failed to add question. Please try again.');
      },
    });
  }

}







