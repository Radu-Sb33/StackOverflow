// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Import testing utilities
// import { RouterTestingModule } from '@angular/router/testing'; // Import RouterTestingModule
// import { ActivatedRoute, Router } from '@angular/router'; // Import Router as well
// import { of } from 'rxjs'; // Import 'of' for creating observable mocks
//
// import { QuestionComponent } from './question.component';
// import { UserService } from '../../services/user.service'; // Assuming UserService path
// import { PostService } from '../../services/post.service'; // Assuming PostService path
//
// describe('QuestionComponent', () => {
//   let component: QuestionComponent;
//   let fixture: ComponentFixture<QuestionComponent>;
//   let httpTestingController: HttpTestingController; // Declare HttpTestingController
//   let userService: UserService; // Declare UserService
//   let postService: PostService; // Declare PostService
//   let router: Router; // Declare Router
//
//   // Mock ActivatedRoute if QuestionComponent uses route parameters (though component.ts doesn't show it directly, keeping it for safety based on previous iteration)
//   const fakeActivatedRoute = {
//     snapshot: {
//       paramMap: {
//         get: (key: string) => 'some-id' // Provide a mock ID if needed
//       }
//     },
//     // If using observable params, mock the params observable
//     params: of({ id: 'some-id' })
//   };
//
//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       imports: [
//         QuestionComponent, // Assuming QuestionComponent is standalone
//         HttpClientTestingModule, // Add HttpClientTestingModule
//         RouterTestingModule // Add RouterTestingModule to provide Router and ActivatedRoute mocks
//         // Add any other necessary Angular module imports
//       ],
//       providers: [
//         UserService, // Provide the UserService
//         PostService, // Provide the PostService
//         // Provide the mocked ActivatedRoute (RouterTestingModule might provide one, but explicit mock gives more control)
//         // { provide: ActivatedRoute, useValue: fakeActivatedRoute } // Keep if needed for specific route param tests
//         // Add any other necessary providers or mocks
//       ]
//     })
//       .compileComponents(); // For components with templates
//
//     fixture = TestBed.createComponent(QuestionComponent);
//     component = fixture.componentInstance;
//     httpTestingController = TestBed.inject(HttpTestingController); // Inject HttpTestingController
//     userService = TestBed.inject(UserService); // Inject UserService
//     postService = TestBed.inject(PostService); // Inject PostService
//     router = TestBed.inject(Router); // Inject Router
//
//     // Trigger change detection to initialize the component and call ngOnInit
//     fixture.detectChanges();
//
//     // Expect the HTTP request made in ngOnInit (postService.getQuestions())
//     // and flush it with a mock response to prevent pending requests.
//     const req = httpTestingController.expectOne('your_questions_api_url'); // Replace with the actual API URL
//     req.flush([]); // Provide a mock empty array of questions or sample data
//   });
//
//   afterEach(() => {
//     // After every test, assert that there are no pending requests.
//     httpTestingController.verify();
//   });
//
//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });
//
//   // Add more tests here to verify the behavior of QuestionComponent
//   // For example:
//
//   it('should set isAuthenticated on init', () => {
//     // You might want to spy on userService.isAuthenticated if its initial value matters
//     // For a simple check, just verify it's set
//     expect(component.isAuthenticated).toBeDefined();
//     // If you need to test specific logic based on auth status, spy on the getter:
//     // spyOnProperty(userService, 'isAuthenticated', 'get').and.returnValue(true);
//     // component.ngOnInit(); // Re-run ngOnInit after changing mock
//     // expect(...)
//   });
//
//   it('should fetch questions on init', () => {
//     // The request was already expected and flushed in beforeEach.
//     // You can add assertions here about the component's state after fetching.
//     // For example, if you flushed with sample data:
//     // expect(component.questions.length).toBe(expected_length);
//   });
//
//   it('should navigate to add-question', () => {
//     const navigateSpy = spyOn(router, 'navigate'); // Spy on the router's navigate method
//     component.navigateToAddQuestion();
//     expect(navigateSpy).toHaveBeenCalledWith(['/add-question']); // Verify navigation
//   });
//
//   it('should set localStorage and navigate to add-answer', () => {
//     const navigateSpy = spyOn(router, 'navigate'); // Spy on the router's navigate method
//     const localStorageSpy = spyOn(localStorage, 'setItem'); // Spy on localStorage.setItem
//     const testQuestionId = 123;
//
//     component.navigateToAddAnswer(testQuestionId);
//
//     expect(localStorageSpy).toHaveBeenCalledWith('idParinte', testQuestionId.toString()); // Verify localStorage was set
//     expect(navigateSpy).toHaveBeenCalledWith(['/add-answer']); // Verify navigation
//   });
//
//   it('should load answers for a question', () => {
//     const testQuestionId = 456;
//     const mockAnswers = [{ id: 1, text: 'Answer 1' }, { id: 2, text: 'Answer 2' }];
//
//     // Add a mock question to the component's questions array for the test
//     component.questions = [{ id: testQuestionId, title: 'Test Question', answers: [] }];
//
//     component.loadAnswers(testQuestionId);
//
//     // Expect the HTTP request for answers
//     const req = httpTestingController.expectOne(`http://localhost:8080/questions/${testQuestionId}/answers`); // Replace with actual API URL
//     expect(req.request.method).toEqual('GET');
//
//     // Flush the request with mock answers
//     req.flush(mockAnswers);
//
//     // Verify that the answers were added to the correct question
//     const questionWithAnswers = component.questions.find(q => q.id === testQuestionId);
//     expect(questionWithAnswers?.answers).toEqual(mockAnswers);
//   });
// });
