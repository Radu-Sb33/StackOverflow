import { Component, OnInit } from '@angular/core'; // Added OnInit
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { PostService } from '../../services/post.service';
import { Router } from '@angular/router';
import {InputText} from "primeng/inputtext";
import {InputTextarea} from "primeng/inputtextarea";
import {ButtonDirective} from "primeng/button";
import {Ripple} from "primeng/ripple";
import {NgIf} from "@angular/common";
import {UserService} from "../../services/user.service";
import {switchMap, catchError} from "rxjs/operators"; // Import catchError
import { throwError } from 'rxjs'; // Import throwError
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
export class AddAnswerComponent implements OnInit{ // Implemented OnInit
  addAnswerForm: FormGroup;
  // Removed title and content as they are handled by the form
  type: PostType | undefined = undefined;
  isLoading: boolean = false; // Added loading indicator

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    public userService: UserService
  ) {
    // Initialize the form group
    this.addAnswerForm = this.fb.group({
      // Title is not needed for an answer based on the form structure in HTML
      content: ['', [Validators.required, Validators.minLength(10)]],
      // Type is fetched asynchronously, not part of the initial form value
    });
  }

  ngOnInit(): void {
    // Fetch the PostType object with id = 2 on component initialization
    this.postService.getType(2).subscribe(
      (response: PostType) => {
        this.type = response; // Assign the fetched PostType object to `type`
        console.log('PostType for Answer loaded:', this.type); // Log success
      },
      (error: HttpErrorResponse) => {
        console.error('Error fetching PostType for Answer:', error.message);
        alert('Failed to load answer type. Please try again.');
      }
    );
  }

  onSubmit() {
    // Check if the form is valid
    if (this.addAnswerForm.invalid) {
      console.log('Form is invalid', this.addAnswerForm.errors);
      // Optionally, display specific validation errors to the user
      return;
    }

    // Check if the PostType has been loaded
    if (!this.type) {
      alert('Answer type is still loading. Please wait a moment and try again.');
      return;
    }

    const { content } = this.addAnswerForm.value;
    const email = localStorage.getItem('emailLogged');

    // Check if the user is logged in
    if (!email) {
      alert('You must be logged in to add an answer.');
      this.router.navigate(['/login']);
      return;
    }

    const idParinteStr = localStorage.getItem('idParinte');
    // Validate and convert idParinte
    const idParinte = idParinteStr !== null ? Number(idParinteStr) : null;

    // Check if idParinte is a valid number
    if (idParinte === null || isNaN(idParinte)) {
      console.error('Invalid or missing parent question ID in localStorage:', idParinteStr);
      alert('Could not find the parent question. Please navigate from the question page.');
      // Optionally navigate back or to an error page
      // this.router.navigate(['/questions']);
      return;
    }

    this.isLoading = true; // Set loading to true

    // Start the observable chain
    this.userService.getUserByEmail(email).pipe(
      // Catch errors during user fetching
      catchError((err) => {
        console.error('Error fetching user:', err);
        alert('Failed to get user information. Please try again.');
        this.isLoading = false; // Reset loading
        return throwError(() => new Error('Failed to get user information')); // Propagate error
      }),
      switchMap((user) => {
        if (!user) {
          // This case should ideally be caught by the catchError above, but kept for safety
          console.error('User not found for email:', email);
          alert('User not found. Please log in again.');
          this.router.navigate(['/login']);
          this.isLoading = false; // Reset loading
          return throwError(() => new Error('User not found'));
        }
        console.log('User fetched:', user);

        // Fetch the parent question
        return this.postService.getByID(idParinte).pipe(
          // Catch errors during parent question fetching
          catchError((err) => {
            console.error('Error fetching parent question:', err);
            alert('Failed to load the parent question. It might have been deleted.');
            this.router.navigate(['/questions']); // Navigate back if parent question not found
            this.isLoading = false; // Reset loading
            return throwError(() => new Error('Failed to load parent question')); // Propagate error
          }),
          switchMap((parentQuestion) => {
            if (!parentQuestion) {
              // This case should ideally be caught by the catchError above, but kept for safety
              console.error('Parent question not found for ID:', idParinte);
              alert('Parent question not found. It might have been deleted.');
              this.router.navigate(['/questions']); // Navigate back
              this.isLoading = false; // Reset loading
              return throwError(() => new Error('Parent question not found'));
            }
            console.log('Parent question fetched:', parentQuestion);

            // Create the answer object
            const answer: Partial<Answer> = {
              createdByUser: user,
              postType: this.type, // Use the fetched type
              postContent: content,
              parentQuestion: parentQuestion, // Include the parent question object
            };
            console.log('Answer object created:', answer);

            // Send the answer to the backend
            return this.postService.createPost(answer);
          })
        );
      })
    ).subscribe({
      next: () => {
        console.log('Answer added successfully!');
        alert('Answer added successfully!');
        this.isLoading = false; // Reset loading
        this.router.navigate(['/questions']); // Navigate back to questions list
      },
      error: (err) => {
        console.error('Error adding answer:', err);
        alert(err.message || 'Failed to add answer. Please try again.');
        this.isLoading = false; // Reset loading
      },
    });
  }
}
