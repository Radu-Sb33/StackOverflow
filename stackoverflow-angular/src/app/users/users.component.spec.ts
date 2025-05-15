// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Import testing utilities
//
// import { UsersComponent } from './users.component';
// import { UserService } from '../services/user.service'; // Assuming UsersComponent uses UserService
//
// describe('UsersComponent', () => {
//   let component: UsersComponent;
//   let fixture: ComponentFixture<UsersComponent>;
//   let httpTestingController: HttpTestingController; // Declare HttpTestingController
//   let userService: UserService; // Declare UserService (if you need to interact with it directly in tests)
//
//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       imports: [
//         UsersComponent, // Assuming UsersComponent is standalone
//         HttpClientTestingModule // Add HttpClientTestingModule here
//         // Add any other necessary imports (e.g., RouterTestingModule if routing is involved)
//       ],
//       providers: [
//         UserService // Provide the UserService that UsersComponent depends on
//         // Add any other necessary providers or mocks here
//       ]
//     })
//       .compileComponents(); // For components with templates
//
//     fixture = TestBed.createComponent(UsersComponent);
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
//   // Add more tests here to verify the behavior of UsersComponent
//   // For example, testing how it fetches and displays a list of users.
// });
