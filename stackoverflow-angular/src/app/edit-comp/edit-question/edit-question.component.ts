// src/app/edit-question/edit-question.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { PostService } from '../../services/post.service';
import { Question } from '../../models/question'; // Ensure this path is correct
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-edit-question',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './edit-question.component.html',
  styleUrls: ['./edit-question.component.scss']
})
export class EditQuestionComponent implements OnInit {
  questionId: number | null = null;
  editForm: FormGroup;
  loading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.editForm = this.fb.group({
      postTitleQ: [''],
      postContent: [''],
      img: [''] // Add the img field here, no initial validator if optional
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.questionId = +idParam; // Convert to number
        this.loadQuestionDetails(this.questionId);
      } else {
        this.errorMessage = 'No question ID provided for editing.';
        // Optionally redirect after a delay if no ID is present
        // setTimeout(() => this.router.navigate(['/questions']), 3000);
      }
    });
  }

  loadQuestionDetails(id: number): void {
    this.loading = true;
    this.postService.getByID(id).subscribe({
      next: (question) => {
        // Check if the loaded question is actually a question type for the form fields
        // Assuming your backend getByID returns the full Post object (which is fine)
        // You might want to refine this check based on `question.postType.typeName`
        this.editForm.patchValue({
          postTitleQ: question.postTitleQ,
          postContent: question.postContent,
          img: question.img // Patch the img value
        });
        this.loading = false;
        this.successMessage = null;
        this.errorMessage = null;
      },
      error: (err) => {
        this.errorMessage = `Failed to load question details: ${err.message || 'Unknown error'}`;
        this.loading = false;
        this.successMessage = null;
        console.error('Error loading question:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.questionId === null) {
      this.errorMessage = 'A question ID is required to update.';
      return; // Stop here if no ID
    }

    if (this.editForm.invalid) {
      // This will only trigger if you have other validators that are failing
      // and the form fields aren't meeting those criteria.
      this.errorMessage = 'One or more fields have invalid input. Please check them.';
      this.editForm.markAllAsTouched(); // To display specific validation messages
      return;
    }

    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;

    // Construct the updatedQuestion object with only the fields your backend expects for update
    const updatedQuestion: Partial<Question> = {
      id: this.questionId, // Crucial: ensure ID is sent in the body for backend to process
      postTitleQ: this.editForm.value.postTitleQ,
      postContent: this.editForm.value.postContent,
      img: this.editForm.value.img // Include the img field
      // Do NOT include fields like createdByUser, postType, answers, etc.,
      // unless your backend update endpoint specifically handles them
      // and expects them from the frontend for an update operation.
      // Your current backend `updatePost` only uses title, content, and img.
    };

    this.postService.updatePost(this.questionId, updatedQuestion).subscribe({
      next: (response) => {
        this.successMessage = 'Question updated successfully!';
        this.loading = false;
        console.log('Update successful:', response);
        // Optionally, navigate back after a short delay
        setTimeout(() => this.router.navigate(['/questions']), 2000);
      },
      error: (err) => {
        // Check if err.error exists for more specific backend error messages
        const backendError = err.error ? (typeof err.error === 'string' ? err.error : err.error.message) : err.message;
        this.errorMessage = `Error updating question: ${backendError || 'Unknown error'}`;
        this.loading = false;
        console.error('Error updating question:', err);
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/questions']);
  }
}
