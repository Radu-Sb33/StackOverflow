// src/app/edit-answer/edit-answer.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms'; // Removed Validators as fields are optional
import { PostService } from '../../services/post.service';
import { Answer } from '../../models/answer'; // Use Post as it encompasses Answer structure
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-edit-answer',
  standalone: true,
  imports: [
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './edit-answer.component.html',
  styleUrls: ['./edit-answer.component.scss']
})
export class EditAnswerComponent implements OnInit {
  answerId: number | null = null;
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
      // Answers typically only have content and optionally an image
      postContent: [''],
      img: ['']
      // No postTitleQ for answers
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.answerId = +idParam; // Convert to number
        this.loadAnswerDetails(this.answerId);
      } else {
        this.errorMessage = 'No answer ID provided for editing.';
        // Optionally redirect after a delay if no ID is present
        // setTimeout(() => this.router.navigate(['/questions']), 3000); // Redirect to questions list
      }
    });
  }

  loadAnswerDetails(id: number): void {
    this.loading = true;
    // Use getByID for answers too, as your backend returns Post objects for all posts
    this.postService.getAnswerByID(id).subscribe({
      next: (answer: Answer) => { // Cast to Post, which handles answer structure
        // Verify it's actually an answer if necessary, using answer.postType.typeName
        if (answer.postType && answer.postType.typename === 'answer') {
          this.editForm.patchValue({
            postContent: answer.postContent,
            img: answer.img
          });
          this.loading = false;
          this.successMessage = null;
          this.errorMessage = null;
        } else {
          this.errorMessage = 'The provided ID does not belong to an answer.';
          this.loading = false;
          console.error('Loaded post is not an answer:', answer);
        }
      },
      error: (err) => {
        this.errorMessage = `Failed to load answer details: ${err.message || 'Unknown error'}`;
        this.loading = false;
        this.successMessage = null;
        console.error('Error loading answer:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.answerId === null) {
      this.errorMessage = 'An answer ID is required to update.';
      return;
    }

    // You might want a check here to ensure at least content or image is provided
    if (!this.editForm.value.postContent && !this.editForm.value.img) {
      this.errorMessage = 'Please provide content or an image URL to update the answer.';
      return;
    }

    if (this.editForm.invalid) {
      this.errorMessage = 'One or more fields have invalid input. Please check them.';
      this.editForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.successMessage = null;
    this.errorMessage = null;

    // Construct the updatedAnswer object with only the fields your backend expects for update
    const updatedAnswer: Partial<Answer> = { // Use Partial<Post> as it's an answer (subset of Post fields)
      id: this.answerId,
      postContent: this.editForm.value.postContent,
      img: this.editForm.value.img
      // No postTitleQ for answers when updating
    };

    this.postService.updatePost(this.answerId, updatedAnswer).subscribe({
      next: (response) => {
        this.successMessage = 'Answer updated successfully!';
        this.loading = false;
        console.log('Update successful:', response);
        // Optionally, navigate back to the question details page after update
        // You'll need the question ID to navigate back to its detail page.
        // If you don't have it, navigate to general questions list.
        setTimeout(() => this.router.navigate(['/questions']), 2000); // Redirect to questions list for now
      },
      error: (err) => {
        const backendError = err.error ? (typeof err.error === 'string' ? err.error : err.error.message) : err.message;
        this.errorMessage = `Error updating answer: ${backendError || 'Unknown error'}`;
        this.loading = false;
        console.error('Error updating answer:', err);
      }
    });
  }

  cancel(): void {
    // Navigate back to the general questions list or a specific question page if you have the parent question ID
    this.router.navigate(['/questions']);
  }
}
