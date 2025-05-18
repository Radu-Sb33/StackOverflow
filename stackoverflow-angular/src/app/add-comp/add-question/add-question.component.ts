import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PostTag } from '../../models/PostTag';
import { Router } from '@angular/router';
import { InputTextModule } from "primeng/inputtext";
import { ButtonModule } from "primeng/button";
import { RippleModule } from "primeng/ripple";
import { NgIf, NgClass } from "@angular/common";
import { UserService } from "../../services/user.service";
import { switchMap, map, catchError, tap, finalize, debounceTime, distinctUntilChanged } from "rxjs/operators";
import { of } from 'rxjs';
import { Question } from "../../models/question";
import { User } from "../../models/user";
// HttpErrorResponse nu mai este necesar direct în componentă dacă serviciul gestionează eroarea
// import { HttpErrorResponse } from "@angular/common/http";
import { PostType } from "../../models/postType";
import { FloatLabelModule } from "primeng/floatlabel";
import { AutoCompleteModule, AutoCompleteCompleteEvent, AutoCompleteSelectEvent } from 'primeng/autocomplete';
import { MessagesModule } from 'primeng/messages';
// @ts-ignore
import { Message } from 'primeng/api';
import { TagService } from '../../services/tag.service';
import { Tag } from '../../models/tag';
import {PostService} from "../../services/post.service";
import {Answer} from "../../models/answer";

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
    FloatLabelModule,
    AutoCompleteModule,
    MessagesModule
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
  selectedTagForForm: Tag | null = null;

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
      tagInput: [null],
      newTagDescriptionCtrl: ['', [Validators.minLength(5)]] // Validatorul 'required' se adaugă dinamic
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

    this.addQuestionForm.get('tagInput')?.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(value => {
        if (typeof value === 'object' && value !== null) {
          this.selectedTagForForm = value as Tag;
          this.showCreateTagInline = false;
          this.addQuestionForm.get('newTagDescriptionCtrl')?.reset('');
        } else if (value === null || (typeof value === 'string' && value.trim() === '')) {
          this.selectedTagForForm = null;
          this.showCreateTagInline = false;
        }
      })
    ).subscribe();
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
    if (this.selectedTagForForm || this.showCreateTagInline || typeof controlValue !== 'string') {
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
          tag.tagName.toLowerCase().includes(cleanedQuery)
        );
      } else {
        filtered = [...this.allTags];
      }
    } else {
      filtered = [...this.allTags];
    }
    this.filteredTags = filtered;
  }

  onTagSelect(event: AutoCompleteSelectEvent): void {
    if (typeof event.value === 'object' && event.value !== null) {
      this.selectedTagForForm = event.value as Tag;
      this.showCreateTagInline = false;
      this.addQuestionForm.get('newTagDescriptionCtrl')?.reset('');
      this.addQuestionForm.get('tagInput')?.setValue(this.selectedTagForForm, { emitEvent: false });
    }
  }

  onTagClear(): void {
    this.selectedTagForForm = null;
    this.showCreateTagInline = false;
    this.newTagFromAutoComplete = '';
  }

  prepareCreateTag(): void {
    const currentTagInputValue = this.addQuestionForm.get('tagInput')?.value;
    if (typeof currentTagInputValue === 'string') {
      const tagName = currentTagInputValue.trim();
      if (tagName !== '') {
        const existingTag = this.allTags.find(t => t.tagName.toLowerCase() === tagName.toLowerCase());
        if (existingTag) {
          this.selectedTagForForm = existingTag;
          this.addQuestionForm.get('tagInput')?.setValue(existingTag, { emitEvent: false });
          this.showCreateTagInline = false;
          this.messages = [{ severity: 'info', summary: 'Tag Selectat', detail: `Tag-ul '${existingTag.tagName}' există deja și a fost selectat.` }];
        } else {
          this.newTagFromAutoComplete = tagName;
          this.selectedTagForForm = null;
          this.showCreateTagInline = true;
          this.addQuestionForm.get('newTagDescriptionCtrl')?.reset('');
          this.addQuestionForm.get('newTagDescriptionCtrl')?.setValidators([Validators.required, Validators.minLength(5)]);
          this.addQuestionForm.get('newTagDescriptionCtrl')?.updateValueAndValidity();
        }
      } else {
        this.messages = [{ severity: 'warn', summary: 'Nume Tag Invalid', detail: 'Numele tag-ului nu poate conține doar spații.' }];
        this.showCreateTagInline = false;
      }
    } else if (typeof currentTagInputValue === 'object' && currentTagInputValue !== null) {
      this.selectedTagForForm = currentTagInputValue as Tag; // Dacă e deja un obiect Tag (ex: selectat și apoi text șters parțial)
      this.showCreateTagInline = false;
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

    // Verifică dacă controlul este invalid SAU dacă are validatorul 'required' și valoarea după trim este goală
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
        this.selectedTagForForm = createdTag;
        this.addQuestionForm.get('tagInput')?.setValue(createdTag, { emitEvent: false });
        this.showCreateTagInline = false;
        this.newTagFromAutoComplete = '';
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

        const questionPayload: Question = {
          id: questionId,
          createdByUser: this.currentUser,
          postType: this.postType!,
          postTitleQ: titleValue,
          postContent: contentValue,
          postedDate: this.addQuestionForm.get('postedDate')?.value,
        };

        console.log('Payload pentru creare întrebare:', questionPayload);

        if (!questionId) {
          throw new Error('Nu s-a putut obține ID-ul întrebării create.');
        }

        if (this.selectedTagForForm && this.selectedTagForForm.id && questionId) {
          const postPayloadForLink: Partial<Question> = {
            id: questionId
          };

          const tagPayloadForLink: Partial<Tag> = {
            id: this.selectedTagForForm.id,
            tagName: this.selectedTagForForm.tagName,
            tagDescription: this.selectedTagForForm.tagDescription,
            createdByUsername: this.selectedTagForForm?.createdByUsername || this.currentUser?.username
          };

          const postTagPayload: Partial<PostTag> = {
            post: postPayloadForLink as Question,
            tag: tagPayloadForLink as Tag
          };

          console.log('Payload NOU pentru creare PostTag:', postTagPayload);

          return this.postService.createPostTag(postTagPayload as PostTag).pipe(
            map(postTagResponse => ({ createdQuestion, postTagResponse })),
            catchError(postTagError => {
              console.error('Eroare la crearea PostTag:', postTagError.message);
              this.messages = [{
                severity: 'warn',
                summary: 'Eroare Legare Tag',
                detail: `Întrebarea a fost creată, dar legarea tag-ului a eșuat: ${postTagError.message || 'Eroare necunoscută'}`
              }];
              return of({ createdQuestion, postTagResponse: null });
            })
          );
        } else {
          return of({ createdQuestion, postTagResponse: null });
        }
      })
    ).subscribe({
      next: ({ createdQuestion, postTagResponse }) => {
        let successMessage = `Întrebarea '${createdQuestion.postTitleQ}' a fost adăugată cu succes!`;
        if (postTagResponse && this.selectedTagForForm) {
          successMessage += ` Tag-ul '${this.selectedTagForForm.tagName}' a fost legat.`;
        } else if (this.selectedTagForForm) {
          successMessage += ` Legarea tag-ului '${this.selectedTagForForm.tagName}' a fost omisă sau a eșuat.`;
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

