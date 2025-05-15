// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Import testing utilities
//
// import { UserProfileComponent } from './user-profile.component';
// import { UserService } from '../services/user.service'; // Assuming UserService is in a '../user.service' file
//
// describe('UserProfileComponent', () => {
//   let component: UserProfileComponent;
//   let fixture: ComponentFixture<UserProfileComponent>;
//   let httpTestingController: HttpTestingController; // Declare HttpTestingController
//   let userService: UserService; // Declare UserService (if you need to interact with it directly in tests)
//
//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       imports: [
//         UserProfileComponent, // Assuming UserProfileComponent is standalone
//         HttpClientTestingModule // Add HttpClientTestingModule here
//         // Add any other necessary imports (e.g., RouterTestingModule if routing is involved)
//       ],
//       providers: [
//         UserService // Provide the UserService that UserProfileComponent depends on
//         // Add any other necessary providers or mocks here
//       ]
//     })
//       .compileComponents(); // For components with templates
//
//     fixture = TestBed.createComponent(UserProfileComponent);
//     component = fixture.componentInstance;
//     httpTestingController = TestBed.inject(HttpTestingController); // Inject HttpTestingController
//     userService = TestBed.inject(UserService); // Inject UserService (if needed for tests)
//     fixture.detectChanges(); // Trigger change detection to initialize the component
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
//   // Add more tests here to verify the behavior of UserProfileComponent
//   // For example, testing how it displays user data fetched via UserService
//   // or how it handles interactions.
// });
