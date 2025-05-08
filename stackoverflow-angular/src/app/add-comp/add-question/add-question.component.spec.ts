import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ReactiveFormsModule, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { AddQuestionComponent } from './add-question.component';
import { PostService } from '../../services/post.service';
import { UserService } from '../../services/user.service';
import { PostType } from '../../models/postType';
import { User } from '../../models/user';
import { Question } from '../../models/question';

class MockPostService {
  getType(id: number) {
    if (id === 1) {
      return of<PostType>({ id: 1, typename: 'question' });
    }

    return of(undefined);
  }

  createPost(question: Partial<Question>) {
    return of<Question>({ id: 123, ...question } as Question);
  }
}

class MockUserService {
  getUserByEmail(email: string) {
    if (email === 'test@example.com') {
      return of<User>({
        id: 99,
        username: "genericuser",
        email: "test@example.com",
        password: "password",
        about: "A generic user.",
        is_moderator: false,
        reputation: 0,
        is_banned: false,
        img: "picture/generic.jpg",
        creation_date: new Date()
      });
    }
    return of(null);
  }
}

class MockRouter {
  navigate = jasmine.createSpy('navigate').and.returnValue(Promise.resolve(true));
}

describe('AddQuestionComponent', () => {
  let component: AddQuestionComponent;
  let fixture: ComponentFixture<AddQuestionComponent>;
  let postService: PostService;
  let userService: UserService;
  let router: Router;

  const mockFetchedPostType: PostType = { id: 1, typename: 'question' };
  const mockLoggedInUser: User = {
    id: 2,
    username: "testuser",
    email: "test@example.com",
    password: "password123",
    about: "A test user.",
    is_moderator: false,
    reputation: 10.0,
    is_banned: false,
    img: "picture/profile_test.jpg",
    creation_date: new Date('2025-01-01T10:00:00')
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AddQuestionComponent,
        ReactiveFormsModule,
      ],
      providers: [
        FormBuilder,
        { provide: PostService, useClass: MockPostService },
        { provide: UserService, useClass: MockUserService },
        { provide: Router, useClass: MockRouter },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AddQuestionComponent);
    component = fixture.componentInstance;

    postService = TestBed.inject(PostService);
    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);


    spyOn(localStorage, 'getItem').and.callFake((key: string) => {
      if (key === 'emailLogged') {
        return 'test@example.com';
      }
      return null;
    });
    spyOn(localStorage, 'setItem').and.stub();
    spyOn(console, 'error').and.stub();
    spyOn(console, 'log').and.stub();
    spyOn(window, 'alert').and.stub();


    spyOn(userService, 'getUserByEmail').and.callFake((email: string) => {
      if (email === 'test@example.com') {
        return of(mockLoggedInUser);
      }
      return of(null);
    });
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should initialize the form in constructor and fetch type in ngOnInit', fakeAsync(() => {
    const getTypeSpy = spyOn(postService, 'getType').and.returnValue(of(mockFetchedPostType));

    fixture.detectChanges();
    tick();

    expect(getTypeSpy).toHaveBeenCalledWith(1);
    expect(component.type).toEqual(mockFetchedPostType);
    expect(component.addQuestionForm).toBeDefined();
    expect(component.addQuestionForm.get('title')).toBeDefined();
    expect(component.addQuestionForm.get('content')).toBeDefined();

    expect(component.addQuestionForm.get('type')?.value).toBeInstanceOf(Object);
  }));

  describe('onSubmit', () => {
    beforeEach(fakeAsync(() => {

      spyOn(postService, 'getType').and.returnValue(of(mockFetchedPostType));

      fixture.detectChanges();
      tick();

      (window.alert as jasmine.Spy).calls.reset();
      (router.navigate as jasmine.Spy).calls.reset();
      (console.log as jasmine.Spy).calls.reset();
      (localStorage.setItem as jasmine.Spy).calls.reset();

    }));

    it('should not submit if form is invalid', () => {
      const createPostSpy = spyOn(postService, 'createPost').and.callThrough();

      component.addQuestionForm.patchValue({ title: '', content: '' });
      expect(component.addQuestionForm.invalid).withContext('Form should be invalid with empty title/content').toBeTrue();

      component.onSubmit();

      expect(createPostSpy).not.toHaveBeenCalled();
    });

    it('should show alert and navigate to login if user is not logged in', () => {
      (localStorage.getItem as jasmine.Spy).and.returnValue(null);
      const createPostSpy = spyOn(postService, 'createPost');


      component.addQuestionForm.patchValue({ title: 'Titlu Valid', content: 'Conținut valid și suficient de lung.' });
      expect(component.addQuestionForm.valid).withContext("Formularul ar trebui să fie valid.").toBeTrue();

      component.onSubmit();

      expect(window.alert).toHaveBeenCalledWith('You must be logged in to add a question.');
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
      expect(createPostSpy).not.toHaveBeenCalled();
    });

    it('should show alert if user is not found by email', fakeAsync(() => {

      (userService.getUserByEmail as jasmine.Spy).and.returnValue(of<User | null>(null));

      const createPostSpy = spyOn(postService, 'createPost');

      component.addQuestionForm.patchValue({ title: 'Titlu Valid', content: 'Conținut valid și suficient de lung.' });
      expect(component.addQuestionForm.valid).withContext("Formularul ar trebui să fie valid.").toBeTrue();

      component.onSubmit();
      tick();

      expect(userService.getUserByEmail).toHaveBeenCalledWith('test@example.com');
      expect(window.alert).toHaveBeenCalledWith('User not found');
      expect(createPostSpy).not.toHaveBeenCalled();
    }));
  });
});
