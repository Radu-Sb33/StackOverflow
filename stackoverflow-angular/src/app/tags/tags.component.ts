import { Component, OnInit } from '@angular/core';
import { Card } from 'primeng/card';
import { TagService } from '../services/tag.service';
import { Tag as PrimeTag } from 'primeng/tag';
import { Tag as AppTag } from '../models/tag';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Button } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { UserService } from '../services/user.service';  // import UserService
import { User } from '../models/user';
import { of } from 'rxjs';
import {log} from "@angular-devkit/build-angular/src/builders/ssr-dev-server";

@Component({
  selector: 'app-tags',
  standalone: true,
  imports: [
    Card,
    PrimeTag,
    CommonModule,
    FormsModule,
    Button, InputTextModule, FloatLabelModule
  ],
  templateUrl: './tags.component.html',
  styleUrl: './tags.component.scss'
})
export class TagsComponent implements OnInit {
  tags: (AppTag & { color?: string })[] = [];
  newTag: Partial<AppTag> = {
    tagName: '',
    tagDescription: ''
  };
  loading: boolean = false;
  currentUser: User | null = null;  // user curent

  constructor(
    private tagService: TagService,
    private userService: UserService  // inject UserService
  ) {}

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe({
      next: user => {
        this.currentUser = user;
        console.log('User curent:', this.currentUser); // ✅ acum merge
      },
      error: err => {
        console.error('Nu s-a putut prelua userul curent:', err);
        this.currentUser = null;
      }
    });

    this.tagService.getTags().subscribe({
      next: (data: AppTag[]) => {
        this.tags = data.map(tag => ({
          ...tag,
          color: this.getRandomColor()
        }));
      },
      error: err => console.error('Eroare la preluarea tagurilor:', err)
    });
  }

  onAddTag(): void {
    if (!this.newTag.tagName || !this.newTag.tagDescription) return;

    if (!this.currentUser) {
      alert('Nu ești logat. Te rog să te autentifici înainte să adaugi tag-uri.');
      return;
    }

    this.loading = true;

    const newTagToSend: Partial<AppTag> = {
      tagName: this.newTag.tagName!,
      tagDescription: this.newTag.tagDescription!,
      createdByUsername: this.currentUser.username
    };

    console.log('Tag to send:', newTagToSend);

    this.tagService.createTag(newTagToSend).subscribe({
      next: (tag) => {
        this.tags.push({ ...tag, color: this.getRandomColor() });
        this.newTag = { tagName: '', tagDescription: '' };
        this.refreshTags();
        this.loading = false;
      },
      error: err => {
        console.error('Eroare la adăugarea tagului:', err);
        alert('Eroare la adăugare.');
        this.loading = false;
      }
    });

  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    return '#' + Array.from({ length: 6 })
      .map(() => letters[Math.floor(Math.random() * 16)])
      .join('');
  }

  refreshTags(): void {
    this.tagService.getTags().subscribe({
      next: (data: AppTag[]) => {
        this.tags = data.map(tag => ({
          ...tag,
          color: this.getRandomColor()
        }));
      },
      error: err => {
        console.error('Eroare la reîncărcarea tagurilor:', err);
      }
    });
  }

}
