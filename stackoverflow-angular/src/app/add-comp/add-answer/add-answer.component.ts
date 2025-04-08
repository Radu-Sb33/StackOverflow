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
import {Answer} from "../../models/answer";

@Component({
  selector: 'app-add-answer',
  standalone: true,
  imports: [ReactiveFormsModule,
    InputText,
    InputTextarea,
    ButtonDirective,
    Ripple,
    NgIf],
  templateUrl: './add-answer.component.html',
  styleUrl: './add-answer.component.scss'
})
export class AddAnswerComponent {
  addAnswerForm: FormGroup;
  title: string='';
  content: string='';
  type: PostType | undefined = undefined;
  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    public userService: UserService
  ) {
    this.addAnswerForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
      type: postService.getType(2)
    });
  }
  ngOnInit(): void {
    // Fetch the PostType object with id = 1 on component initialization
    this.postService.getType(2).subscribe(
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
    if (this.addAnswerForm.invalid) {
      return;
    }

    const { content } = this.addAnswerForm.value;

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
        const answer: Partial<Answer> = {
          createdByUser: user,
          postType: this.type,
          postContent: content,
         // parentQuestion: this.postService.getByID(localStorage.getItem('idParinte'));
        };

        // Send the question to the backend
        return this.postService.createPost(answer);
      })
    ).subscribe({
      next: () => {
        alert('Question added successfully!');
        this.router.navigate(['/questions']);
      },
      error: (err) => {
        console.error('Error adding question:', err);
        alert(err.message || 'Failed to add question. Please try again.');
      },
    });
  }


}
