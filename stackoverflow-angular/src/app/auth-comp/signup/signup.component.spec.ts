// import { ComponentFixture, TestBed } from '@angular/core/testing';
// import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Import testing utilities
// import { RouterTestingModule } from '@angular/router/testing'; // Import RouterTestingModule
// import { FormsModule } from '@angular/forms'; // Import FormsModule
// import { of, throwError } from 'rxjs'; // Import 'of' and 'throwError' for mocking observables
// import { HttpErrorResponse } from '@angular/common/http'; // Import HttpErrorResponse
//
// import { SignupComponent } from './signup.component';
// import { UserService } from '../../services/user.service'; // Assuming UserService path
// import { User } from '../../models/user'; // Import User model
//
// describe('SignupComponent', () => {
//   let component: SignupComponent;
//   let fixture: ComponentFixture<SignupComponent>;
//   let httpTestingController: HttpTestingController; // Declare HttpTestingController
//   let userService: UserService; // Declare UserService
//
//   // Define the base URL from the UserService
//   const baseUrl = 'http://localhost:8080/user';
//
//   beforeEach(async () => {
//     await TestBed.configureTestingModule({
//       imports: [
//         SignupComponent, // Assuming SignupComponent is standalone
//         HttpClientTestingModule, // Add HttpClientTestingModule
//         RouterTestingModule, // Add RouterTestingModule for RouterLink
//         FormsModule // Add FormsModule for ngModel
//       ],
//       providers: [
//         UserService // Provide the UserService
//         // Add any other necessary providers or mocks here
//       ]
//     })
//       .compileComponents(); // For components with templates
//
//     fixture = TestBed.createComponent(SignupComponent);
//     component = fixture.componentInstance;
//     userService = TestBed.inject(UserService); // Inject UserService
//     httpTestingController = TestBed.inject(HttpTestingController); // Inject HttpTestingController
//
//     // Spy on the getUsers method which is called in ngOnInit
//     // We need to mock its response to prevent a real HTTP call during setup
//     spyOn(userService, 'getUsers').and.returnValue(of([])); // Return an observable with an empty array
//
//     // Spy on window.alert to prevent it from blocking tests
//     spyOn(window, 'alert');
//
//     // Trigger change detection to initialize the component and call ngOnInit
//     fixture.detectChanges();
//
//     // Expect and flush the HTTP request made by userService.getUsers() in ngOnInit
//     // This is required because we spied on getUsers but it still internally uses HttpClient
//     const req = httpTestingController.expectOne(`${baseUrl}/getAllUsers`); // Use the actual URL
//     req.flush([]); // Flush with mock data (e.g., an empty array)
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
//   // Add tests for the register method and other component logic
//
//   it('should show invalid email message for invalid email format', () => {
//     component.user.email = 'invalid-email';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     component.register();
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('Invalid email format. Please enter a valid email address.');
//     // Ensure no HTTP requests were made
//     httpTestingController.expectNone(`${baseUrl}/check-email?email=invalid-email`); // Use the actual URL
//   });
//
//   it('should show password mismatch message if passwords do not match', () => {
//     component.user.email = 'test@example.com';
//     component.user.username = 'testuser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'differentpassword';
//
//     component.register();
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('Passwords do not match. Please confirm your password correctly.');
//     // Ensure no HTTP requests were made
//     httpTestingController.expectNone(`${baseUrl}/check-email?email=test@example.com`); // Use the actual URL
//   });
//
//   it('should show email exists message if email is already registered', () => {
//     component.user.email = 'existing@example.com';
//     component.user.username = 'testuser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     // Spy on checkEmailExists and make it return true
//     spyOn(userService, 'checkEmailExists').and.returnValue(of(true));
//
//     component.register();
//
//     // Expect the checkEmailExists request and flush it (although the spy handles the return value,
//     // expectOne is good practice to ensure the request was attempted)
//     const req = httpTestingController.expectOne(`${baseUrl}/check-email?email=existing@example.com`); // Use the actual URL
//     req.flush(true); // Flush with the mock response
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('This email is already registered. Please use a different email.');
//     // Ensure checkUsernameExists and register were not called
//     spyOn(userService, 'checkUsernameExists');
//     spyOn(userService, 'register');
//     expect(userService.checkUsernameExists).not.toHaveBeenCalled();
//     expect(userService.register).not.toHaveBeenCalled();
//   });
//
//   it('should show username exists message if username is already taken', () => {
//     component.user.email = 'new@example.com';
//     component.user.username = 'existinguser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     // Spy on checkEmailExists and make it return false
//     spyOn(userService, 'checkEmailExists').and.returnValue(of(false));
//     // Spy on checkUsernameExists and make it return true
//     spyOn(userService, 'checkUsernameExists').and.returnValue(of(true));
//
//     component.register();
//
//     // Expect and flush the checkEmailExists request
//     httpTestingController.expectOne(`${baseUrl}/check-email?email=new@example.com`).flush(false); // Use the actual URL
//
//     // Expect and flush the checkUsernameExists request
//     httpTestingController.expectOne(`${baseUrl}/check-username?username=existinguser`).flush(true); // Use the actual URL
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('This username is already taken. Please choose another one.');
//     // Ensure register was not called
//     spyOn(userService, 'register');
//     expect(userService.register).not.toHaveBeenCalled();
//   });
//
//   it('should register user successfully if email and username are available', () => {
//     component.user.email = 'new@example.com';
//     component.user.username = 'newuser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     // Spy on checkEmailExists and make it return false
//     spyOn(userService, 'checkEmailExists').and.returnValue(of(false));
//     // Spy on checkUsernameExists and make it return false
//     spyOn(userService, 'checkUsernameExists').and.returnValue(of(false));
//     // Spy on register and make it return an observable that completes (or returns a success response)
//     spyOn(userService, 'register').and.returnValue(of({} as User)); // Return an empty object or success response
//
//     component.register();
//
//     // Expect and flush the checkEmailExists request
//     httpTestingController.expectOne(`${baseUrl}/check-email?email=new@example.com`).flush(false); // Use the actual URL
//
//     // Expect and flush the checkUsernameExists request
//     httpTestingController.expectOne(`${baseUrl}/check-username?username=newuser`).flush(false); // Use the actual URL
//
//     // Expect and flush the register request
//     const registerReq = httpTestingController.expectOne(`${baseUrl}/createUser`); // Use the actual URL
//     expect(registerReq.request.method).toEqual('POST'); // Verify the HTTP method
//     expect(registerReq.request.body).toEqual(component.user); // Verify the request body
//     registerReq.flush({} as User); // Flush with a success response
//
//     expect(component.registrationSuccess).toBeTrue();
//     expect(component.message).toBe('Registration successful! You can now log in.');
//   });
//
//   it('should show error message if email availability check fails', () => {
//     component.user.email = 'test@example.com';
//     component.user.username = 'testuser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     // Spy on checkEmailExists and make it return an error observable
//     spyOn(userService, 'checkEmailExists').and.returnValue(throwError(() => new Error('Email check failed')));
//
//     component.register();
//
//     // Expect and flush the checkEmailExists request with an error
//     httpTestingController.expectOne(`${baseUrl}/check-email?email=test@example.com`).error(new ErrorEvent('Network error')); // Simulate network error
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('Unable to verify email availability.');
//   });
//
//   it('should show error message if username availability check fails', () => {
//     component.user.email = 'new@example.com';
//     component.user.username = 'testuser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     // Spy on checkEmailExists and make it return false
//     spyOn(userService, 'checkEmailExists').and.returnValue(of(false));
//     // Spy on checkUsernameExists and make it return an error observable
//     spyOn(userService, 'checkUsernameExists').and.returnValue(throwError(() => new Error('Username check failed')));
//
//     component.register();
//
//     // Expect and flush the checkEmailExists request
//     httpTestingController.expectOne(`${baseUrl}/check-email?email=new@example.com`).flush(false); // Use the actual URL
//
//     // Expect and flush the checkUsernameExists request with an error
//     httpTestingController.expectOne(`${baseUrl}/check-username?username=testuser`).error(new ErrorEvent('Network error')); // Simulate network error
//
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('Unable to verify username availability.');
//   });
//
//   it('should show error message if registration fails', () => {
//     component.user.email = 'new@example.com';
//     component.user.username = 'newuser';
//     component.user.password = 'password123';
//     component.confirmPassword = 'password123';
//
//     // Spy on checkEmailExists and make it return false
//     spyOn(userService, 'checkEmailExists').and.returnValue(of(false));
//     // Spy on checkUsernameExists and make it return false
//     spyOn(userService, 'checkUsernameExists').and.returnValue(of(false));
//     // Spy on register and make it return an error observable
//     spyOn(userService, 'register').and.returnValue(throwError(() => new Error('Registration failed')));
//
//     component.register();
//
//     // Expect and flush the checkEmailExists request
//     httpTestingController.expectOne(`${baseUrl}/check-email?email=new@example.com`).flush(false); // Use the actual URL
//
//     // Expect and flush the checkUsernameExists request
//     httpTestingController.expectOne(`${baseUrl}/check-username?username=newuser`).flush(false); // Use the actual URL
//
//     // Expect and flush the register request with an error
//     httpTestingController.expectOne(`${baseUrl}/createUser`).error(new ErrorEvent('Network error')); // Simulate network error
//
//     expect(component.registrationSuccess).toBeFalse();
//     expect(component.message).toBe('An error occurred during registration. Please try again.');
//   });
//
//   it('should call window.alert on getUsers error', () => {
//     // We need to re-run ngOnInit to trigger the error path
//     // First, detach the fixture to prevent the initial ngOnInit from interfering
//     fixture.destroy();
//     TestBed.resetTestingModule(); // Reset the testing module
//
//     // Re-configure TestBed for this specific test
//     TestBed.configureTestingModule({
//       imports: [
//         SignupComponent,
//         HttpClientTestingModule,
//         RouterTestingModule,
//         FormsModule
//       ],
//       providers: [
//         UserService
//       ]
//     }).compileComponents();
//
//     fixture = TestBed.createComponent(SignupComponent);
//     component = fixture.componentInstance;
//     userService = TestBed.inject(UserService);
//     httpTestingController = TestBed.inject(HttpTestingController);
//
//     // Spy on getUsers and make it return an error observable
//     spyOn(userService, 'getUsers').and.returnValue(throwError(() => new HttpErrorResponse({ status: 500, statusText: 'Internal Server Error', error: { message: 'Error fetching users' } })));
//
//     // Spy on window.alert
//     const alertSpy = spyOn(window, 'alert');
//
//     // Trigger ngOnInit
//     fixture.detectChanges();
//
//     // Expect and flush the getUsers request with an error
//     const req = httpTestingController.expectOne(`${baseUrl}/getAllUsers`); // Use the actual URL
//     req.error(new ErrorEvent('Network error'), { status: 500 }); // Simulate network error
//
//     // Verify that window.alert was called with the error message
//     expect(alertSpy).toHaveBeenCalledWith('Error fetching users');
//
//     // Verify no pending requests after handling the error
//     httpTestingController.verify();
//   });
// });
