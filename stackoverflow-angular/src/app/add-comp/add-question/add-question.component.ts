import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PostTag } from '../../models/PostTag';
import { Router } from '@angular/router';
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { RippleModule } from "primeng/ripple";
import { NgIf, NgClass, NgFor } from "@angular/common"; // Import NgFor
import { UserService } from "../../services/user.service";
import { switchMap, map, catchError, tap, finalize, debounceTime, distinctUntilChanged } from "rxjs/operators";
import { of, forkJoin } from 'rxjs'; // Import forkJoin
import { Question } from "../../models/question";
import { User } from "../../models/user";
import { PostType } from "../../models/postType";
import { FloatLabelModule } from "primeng/floatlabel";
import { AutoCompleteModule, AutoCompleteCompleteEvent, AutoCompleteSelectEvent } from 'primeng/autocomplete';
import { MessagesModule } from 'primeng/messages';
// @ts-ignore
import { Message } from 'primeng/api';
import { TagService } from '../../services/tag.service';
import { Tag } from '../../models/tag';
import {PostService} from "../../services/post.service";
import { ChipModule } from 'primeng/chip'; // Import ChipModule

@Component({
  selector: 'app-add-question',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    InputTextModule,
    ButtonModule,
    RippleModule,
    NgIf,
    NgClass,
    NgFor, // Add NgFor here
    FloatLabelModule,
    AutoCompleteModule,
    MessagesModule,
    ChipModule // Add ChipModule here
  ],
  templateUrl: './add-question.component.html',
  styleUrl: './add-question.component.scss'
})
export class AddQuestionComponent implements OnInit {
  addQuestionForm: FormGroup;
  postType: PostType | undefined = undefined;
  currentUser: User | null = null;

  allTags: Tag[] = [];
  filteredTags: Tag[] = [];
  selectedTagsForForm: Tag[] = []; // Changed to an array

  showCreateTagInline: boolean = false;
  newTagFromAutoComplete: string = '';
  tagCreationLoading: boolean = false;
  messages: Message[] = [];

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private router: Router,
    public userService: UserService,
    private tagService: TagService
  ) {
    this.addQuestionForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(5)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
      tagInput: [null], // This will temporarily hold the tag selected from autocomplete or the text for a new tag
      newTagDescriptionCtrl: ['', [Validators.minLength(5)]]
    });
  }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(user => {
      this.currentUser = user;
    });

    this.postService.getType(1).subscribe({
      next: (response: PostType) => { this.postType = response; },
      error: (err: Error) => {
        console.error('Eroare la preluarea PostType:', err.message);
        this.messages = [{ severity: 'error', summary: 'Eroare Încărcare Tip', detail: err.message }];
      }
    });

    this.loadTags();

  }

  loadTags(): void {
    this.tagService.getTags().subscribe({
      next: (tags) => { this.allTags = tags; },
      error: (err: Error) => {
        console.error('Eroare la preluarea tag-urilor:', err.message);
        this.messages = [{ severity: 'error', summary: 'Eroare Încărcare Tag-uri', detail: err.message }];
      }
    });
  }

  get showCreateTagPrompt(): boolean {
    const controlValue = this.addQuestionForm.get('tagInput')?.value;
    if (this.showCreateTagInline || typeof controlValue !== 'string') {
      return false;
    }
    const trimmedValue = controlValue.trim();
    if (trimmedValue === '') {
      return false;
    }
    return !this.allTags.some(tag => tag.tagName.toLowerCase() === trimmedValue.toLowerCase());
  }

  get currentTagInputValueForDisplay(): string {
    const controlValue = this.addQuestionForm.get('tagInput')?.value;
    if (typeof controlValue === 'string') {
      return controlValue.trim();
    }
    return '';
  }


  filterTags(event: AutoCompleteCompleteEvent): void {
    let query = event.query;
    let filtered: Tag[] = [];
    if (typeof query === 'string') {
      const cleanedQuery = query.trim().toLowerCase();
      if (cleanedQuery) {
        filtered = this.allTags.filter(tag =>
          tag.tagName.toLowerCase().includes(cleanedQuery) &&
          !this.selectedTagsForForm.some(selected => selected.id === tag.id) // Filter out already selected tags
        );
      } else {
        filtered = this.allTags.filter(tag =>
          !this.selectedTagsForForm.some(selected => selected.id === tag.id)
        );
      }
    } else {
      filtered = this.allTags.filter(tag =>
        !this.selectedTagsForForm.some(selected => selected.id === tag.id)
      );
    }
    this.filteredTags = filtered;
  }

  onTagSelect(event: AutoCompleteSelectEvent): void {
    if (typeof event.value === 'object' && event.value !== null) {
      const selectedTag = event.value as Tag;
      // Add tag only if not already in selectedTagsForForm
      if (!this.selectedTagsForForm.some(tag => tag.id === selectedTag.id)) {
        this.selectedTagsForForm.push(selectedTag);
      }
      this.showCreateTagInline = false;
      this.addQuestionForm.get('newTagDescriptionCtrl')?.reset('');
      this.addQuestionForm.get('tagInput')?.setValue(null); // Clear the input after selection
    }
  }

  removeTag(tagToRemove: Tag): void {
    this.selectedTagsForForm = this.selectedTagsForForm.filter(tag => tag.id !== tagToRemove.id);
    this.filterTags({ query: this.addQuestionForm.get('tagInput')?.value || '' } as AutoCompleteCompleteEvent); // Re-filter to potentially show the removed tag again
  }

  onTagClear(): void {
    // This is for the autocomplete clear button, not needed if we manage tags as chips
    // this.selectedTagForForm = null;
    // this.showCreateTagInline = false;
    // this.newTagFromAutoComplete = '';
  }

  prepareCreateTag(): void {
    const currentTagInputValue = this.addQuestionForm.get('tagInput')?.value;
    if (typeof currentTagInputValue === 'string') {
      const tagName = currentTagInputValue.trim();
      if (tagName !== '') {
        const existingTag = this.allTags.find(t => t.tagName.toLowerCase() === tagName.toLowerCase());
        if (existingTag) {
          // If tag exists and is not already selected, add it to selectedTagsForForm
          if (!this.selectedTagsForForm.some(tag => tag.id === existingTag.id)) {
            this.selectedTagsForForm.push(existingTag);
            this.messages = [{ severity: 'info', summary: 'Tag Selectat', detail: `Tag-ul '${existingTag.tagName}' există deja și a fost adăugat.` }];
          } else {
            this.messages = [{ severity: 'info', summary: 'Tag Deja Adăugat', detail: `Tag-ul '${existingTag.tagName}' este deja în listă.` }];
          }
          this.showCreateTagInline = false;
          this.addQuestionForm.get('tagInput')?.setValue(null); // Clear the input
        } else {
          this.newTagFromAutoComplete = tagName;
          this.showCreateTagInline = true;
          this.addQuestionForm.get('newTagDescriptionCtrl')?.reset('');
          this.addQuestionForm.get('newTagDescriptionCtrl')?.setValidators([Validators.required, Validators.minLength(5)]);
          this.addQuestionForm.get('newTagDescriptionCtrl')?.updateValueAndValidity();
        }
      } else {
        this.messages = [{ severity: 'warn', summary: 'Nume Tag Invalid', detail: 'Numele tag-ului nu poate conține doar spații.' }];
        this.showCreateTagInline = false;
      }
    } else {
      this.messages = [{ severity: 'warn', summary: 'Nume Tag Necesar', detail: 'Te rog, introdu un nume pentru tag.' }];
      this.showCreateTagInline = false;
    }
  }

  cancelCreateTag(): void {
    this.showCreateTagInline = false;
    this.addQuestionForm.get('newTagDescriptionCtrl')?.reset('');
    this.addQuestionForm.get('newTagDescriptionCtrl')?.clearValidators();
    this.addQuestionForm.get('newTagDescriptionCtrl')?.setValidators([Validators.minLength(5)]); // Revine la validarea inițială
    this.addQuestionForm.get('newTagDescriptionCtrl')?.updateValueAndValidity();
  }

  confirmCreateTag(): void {
    const tagName = this.newTagFromAutoComplete ? this.newTagFromAutoComplete.trim() : '';
    if (!tagName) {
      this.messages = [{ severity: 'warn', summary: 'Validare', detail: 'Numele tag-ului lipsește sau este invalid.' }];
      return;
    }

    const descriptionControl = this.addQuestionForm.get('newTagDescriptionCtrl');
    const descriptionValue = descriptionControl?.value ? String(descriptionControl.value).trim() : '';

    if (descriptionControl?.invalid || (descriptionControl?.hasValidator(Validators.required) && descriptionValue === '')) {
      this.messages = [{ severity: 'warn', summary: 'Validare', detail: 'Descrierea tag-ului este obligatorie și trebuie să aibă minim 5 caractere valide.' }];
      descriptionControl?.markAsTouched();
      return;
    }

    if (!this.currentUser || !this.currentUser.username) {
      this.messages = [{ severity: 'error', summary: 'Eroare Utilizator', detail: 'Utilizator nelogat sau nume de utilizator lipsă. Nu se poate crea tag-ul.' }];
      return;
    }

    this.tagCreationLoading = true;
    const tagToCreate: Partial<Tag> = {
      tagName: tagName,
      tagDescription: descriptionValue,
      createdByUsername: this.currentUser.username
    };

    this.tagService.createTag(tagToCreate).pipe(
      finalize(() => this.tagCreationLoading = false)
    ).subscribe({
      next: (createdTag) => {
        this.messages = [{ severity: 'success', summary: 'Succes', detail: `Tag-ul '${createdTag.tagName}' a fost creat.` }];
        this.allTags.push(createdTag);
        // Add the newly created tag to the selected tags
        if (!this.selectedTagsForForm.some(tag => tag.id === createdTag.id)) {
          this.selectedTagsForForm.push(createdTag);
        }
        this.showCreateTagInline = false;
        this.newTagFromAutoComplete = '';
        this.addQuestionForm.get('tagInput')?.setValue(null); // Clear the input
        descriptionControl?.reset('');
        descriptionControl?.clearValidators();
        descriptionControl?.setValidators([Validators.minLength(5)]);
        descriptionControl?.updateValueAndValidity();
      },
      error: (err: Error) => {
        console.error('Eroare la crearea tag-ului:', err.message);
        this.messages = [{ severity: 'error', summary: 'Eroare Creare Tag', detail: err.message }];
      }
    });
  }

  onSubmit() {
    this.messages = []; // Resetează mesajele la fiecare submit
    const titleControl = this.addQuestionForm.get('title');
    const contentControl = this.addQuestionForm.get('content');

    const titleValue = titleControl?.value ? String(titleControl.value).trim() : '';
    const contentValue = contentControl?.value ? String(contentControl.value).trim() : '';

    // Validare titlu
    if (!titleValue) {
      titleControl?.markAsTouched();
      this.messages.push({ severity: 'warn', summary: 'Validare Eșuată', detail: 'Titlul este obligatoriu.' });
    } else if (titleControl?.hasError('minlength')) {
      titleControl?.markAsTouched();
      this.messages.push({ severity: 'warn', summary: 'Validare Eșuată', detail: 'Titlul trebuie să aibă minim 5 caractere valide.' });
    }

    // Validare conținut
    if (!contentValue) {
      contentControl?.markAsTouched();
      this.messages.push({ severity: 'warn', summary: 'Validare Eșuată', detail: 'Conținutul este obligatoriu.' });
    } else if (contentControl?.hasError('minlength')) {
      contentControl?.markAsTouched();
      this.messages.push({ severity: 'warn', summary: 'Validare Eșuată', detail: 'Conținutul trebuie să aibă minim 10 caractere valide.' });
    }

    // Validare tag în curs de creare
    if (this.showCreateTagInline) {
      const descriptionControl = this.addQuestionForm.get('newTagDescriptionCtrl');
      const descriptionValue = descriptionControl?.value ? String(descriptionControl.value).trim() : '';
      if (descriptionControl?.invalid || (descriptionControl?.hasValidator(Validators.required) && descriptionValue === '')) {
        this.messages.push({
          severity: 'warn',
          summary: 'Validare Tag',
          detail: 'Completează descrierea pentru noul tag (minim 5 caractere valide) sau anulează crearea lui.'
        });
        descriptionControl?.markAsTouched();
      }
    }

    if (this.messages.length > 0) {
      return; // Oprește execuția dacă există erori de validare
    }

    if (!this.postType) {
      this.messages = [{
        severity: 'error',
        summary: 'Eroare Configurare',
        detail: 'Tipul întrebării nu este încărcat. Te rog, așteaptă sau reîmprospătează pagina.'
      }];
      return;
    }

    const email = localStorage.getItem('emailLogged');
    if (!email) {
      this.messages = [{
        severity: 'error',
        summary: 'Autentificare Necesară',
        detail: 'Trebuie să fii logat pentru a adăuga o întrebare.'
      }];
      this.router.navigate(['/login']);
      return;
    }

    const userSource = this.currentUser ? of(this.currentUser) : this.userService.getUserByEmail(email);

    userSource.pipe(
      switchMap((user) => {
        if (!user) {
          this.router.navigate(['/login']);
          throw new Error('Utilizatorul nu a fost găsit. Te rog să te autentifici.');
        }
        this.currentUser = user;

        const questionPayload: Partial<Question> = {
          createdByUser: user,
          postType: this.postType!,
          postTitleQ: titleValue,
          postContent: contentValue,
        };

        return this.postService.createPost(questionPayload);
      }),
      switchMap((createdQuestion: Question) => {
        const questionId = createdQuestion.id;

        if (!questionId) {
          throw new Error('Nu s-a putut obține ID-ul întrebării create.');
        }

        // Prepare PostTag creation for all selected tags
        const tagCreationObservables = this.selectedTagsForForm.map(selectedTag => {
          const postPayloadForLink: Partial<Question> = { id: questionId };
          const tagPayloadForLink: Partial<Tag> = {
            id: selectedTag.id,
            tagName: selectedTag.tagName,
            tagDescription: selectedTag.tagDescription,
            createdByUsername: selectedTag?.createdByUsername || this.currentUser?.username
          };

          const postTagPayload: Partial<PostTag> = {
            post: postPayloadForLink as Question,
            tag: tagPayloadForLink as Tag
          };

          console.log('Payload pentru creare PostTag:', postTagPayload);

          return this.postService.createPostTag(postTagPayload as PostTag).pipe(
            catchError(postTagError => {
              console.error(`Eroare la crearea PostTag pentru tag-ul '${selectedTag.tagName}':`, postTagError.message);
              this.messages.push({
                severity: 'warn',
                summary: 'Eroare Legare Tag',
                detail: `Întrebarea a fost creată, dar legarea tag-ului '${selectedTag.tagName}' a eșuat: ${postTagError.message || 'Eroare necunoscută'}`
              });
              return of(null); // Return null for failed tag associations
            })
          );
        });

        // Use forkJoin to wait for all tag associations to complete
        return forkJoin(tagCreationObservables).pipe(
          map(postTagResponses => ({ createdQuestion, postTagResponses }))
        );
      })
    ).subscribe({
      next: ({ createdQuestion, postTagResponses }) => {
        let successMessage = `Întrebarea '${createdQuestion.postTitleQ}' a fost adăugată cu succes!`;
        const successfullyLinkedTags = postTagResponses.filter(response => response !== null);
        if (successfullyLinkedTags.length > 0) {
          successMessage += ` ${successfullyLinkedTags.length} tag(uri) au fost legate.`;
        } else if (this.selectedTagsForForm.length > 0) {
          successMessage += ` Legarea tag-urilor a eșuat parțial sau complet.`;
        }

        alert(successMessage); // Recomand: PrimeNG Toast
        this.router.navigate(['/questions']);
      },
      error: (err: Error) => {
        console.error('Eroare la adăugarea întrebării sau PostTag:', err.message);
        this.messages = [{
          severity: 'error',
          summary: 'Eroare Trimitere',
          detail: err.message
        }];
      }
    });
  }

}
