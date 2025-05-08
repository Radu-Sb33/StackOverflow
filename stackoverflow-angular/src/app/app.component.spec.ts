import { TestBed, ComponentFixture } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { PrimeNG } from 'primeng/config'; // Import PrimeNG service
import { UserService } from './services/user.service'; // Import UserService
import { NO_ERRORS_SCHEMA } from '@angular/core'; // For ignoring unknown PrimeNG elements
import { RouterTestingModule } from '@angular/router/testing'; // For RouterLink, RouterOutlet

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let mockPrimeNG: any;
  let mockUserService: jasmine.SpyObj<UserService>;

  beforeEach(async () => {

    mockPrimeNG = {
      ripple: {
        set: jasmine.createSpy('set')
      }
    };

    mockUserService = jasmine.createSpyObj('UserService', ['logout']);

    await TestBed.configureTestingModule({
      imports: [
        AppComponent,
        RouterTestingModule
      ],
      providers: [
        { provide: PrimeNG, useValue: mockPrimeNG },
        { provide: UserService, useValue: mockUserService }
      ],

      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it(`should have the 'stackoverflow-angular' title`, () => {
    expect(component.title).toEqual('stackoverflow-angular');
  });

});
