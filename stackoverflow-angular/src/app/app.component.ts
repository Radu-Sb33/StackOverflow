// src/app/app.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { PrimeNG } from 'primeng/config';
import { Menubar } from 'primeng/menubar';
import { Avatar } from 'primeng/avatar';
import { OverlayPanel, OverlayPanelModule } from 'primeng/overlaypanel';
import { Ripple } from 'primeng/ripple';
import { InputText } from 'primeng/inputtext';
import { Badge } from 'primeng/badge';
import { NgClass, NgIf } from '@angular/common';
import { Subscription } from 'rxjs';

import { UserService } from "./services/user.service";
import { User } from './models/user'; // Asigură-te că modelul User este corect importat

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet, RouterLink, RouterLinkActive,
    Menubar, Avatar, OverlayPanelModule, Ripple, InputText, Badge,
    NgClass, NgIf
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit, OnDestroy {
  items: MenuItem[] | undefined;
  title = 'stackoverflow-angular';
  initial: string | undefined = '';
  currentUser: User | null = null;
  private userSubscription: Subscription | undefined;

  constructor(
    private primengConfigService: PrimeNG,
    public userService: UserService, // Făcut public pentru acces facil în template
    private router: Router // Injectează Router pentru navigare
  ) {}

  ngOnInit() {
    // Configurare Ripple
    this.userService.clearSession();
    if (this.primengConfigService.ripple && typeof this.primengConfigService.ripple.set === 'function') {
      this.primengConfigService.ripple.set(true);
    } else {
      // Fallback dacă .set nu există sau .ripple este un boolean simplu
      (this.primengConfigService as any).ripple = true;
      console.warn('PrimeNG ripple.set() method not available or ripple is a direct boolean. Attempted direct assignment.');
    }

    this.loadMenuItems();
    this.loadCurrentUser(); // Încarcă datele utilizatorului la inițializare dacă este autentificat
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe(); // Previne memory leaks
    }
  }

  loadMenuItems(): void {
    this.items = [
      // Item-ul "Home" a fost eliminat, logo-ul servește acest scop
      ...(!this.userService.isAuthenticated ? [ // Folosim booleanul direct
        { label: 'Login', icon: 'fa-solid fa-right-to-bracket', routerLink: ['/login'] },
        { label: 'Signup', icon: 'fa-solid fa-user-plus', routerLink: ['/signup'] },
      ] : []),
      { label: 'Questions', icon: 'fa-solid fa-question', routerLink: ['/questions'] },
      { label: 'Answers', icon: 'fa-solid fa-wand-magic-sparkles', routerLink: ['/answers']},
      { label: 'Users', icon: 'bi bi-people', routerLink: ['/users']},
      { label: 'Tags', icon: 'fa-solid fa-tags', routerLink: ['/tags']},
      ...(this.userService.isAuthenticated ? [ // Folosim booleanul direct
        {
          label: 'Logout',
          icon: 'fa-solid fa-sign-out-alt',
          command: () => { this.handleLogout(); },
        }
      ] : [])
    ];
  }

  loadCurrentUser(): void {
    if (this.userService.isAuthenticated) { // Folosim booleanul direct
      const userEmail = localStorage.getItem('emailLogged');
      this.initial = this.currentUser?.username.charAt(0).toUpperCase();
      console.log('Initial',this.initial);
      if (userEmail) {
        if (this.userSubscription) {
          this.userSubscription.unsubscribe();
        }
        this.userSubscription = this.userService.getUserByEmail(userEmail).subscribe({
          next: (userData: User) => {
            this.currentUser = userData;
          },
          error: (err) => {
            console.error('Error fetching user data:', err);
            this.currentUser = null;
            // Dacă nu putem lua datele userului deși e marcat ca autentificat, forțăm logout
            if (this.userService.isAuthenticated) {
              this.userService.logout(); // Aceasta va seta isAuthenticated la false și va naviga
              this.loadMenuItems();      // Actualizează meniul
            }
          }
        });
      } else {
        // Acest caz nu ar trebui să se întâmple dacă login funcționează corect
        console.warn('Logged in user email not found in localStorage. Forcing logout if needed.');
        this.currentUser = null;
        if (this.userService.isAuthenticated) {
          this.userService.logout();
          this.loadMenuItems();
        }
      }
    } else {
      this.currentUser = null; // Asigură-te că currentUser e null dacă nu e autentificat
    }
  }

  toggleUserPanel(event: Event, userOverlayPanel: OverlayPanel): void {
    if (this.userService.isAuthenticated) { // Folosim booleanul direct
      if (!this.currentUser && localStorage.getItem('emailLogged')) {
        // Dacă este autentificat dar datele nu sunt încărcate, încearcă să le încarci
        this.loadCurrentUser();
      }
      // Deschide panoul doar dacă este autentificat și datele sunt (sau vor fi) disponibile
      // Verificarea this.currentUser aici asigură că nu deschidem un panou gol dacă loadCurrentUser eșuează asincron
      if (this.currentUser || localStorage.getItem('emailLogged')) {
        userOverlayPanel.toggle(event);
      } else {
        console.warn("User is authenticated but no current user data or email found. Not opening panel.");
      }
    } else {
      // Dacă utilizatorul NU este autentificat, redirecționează la pagina de login
      console.log("User not authenticated, redirecting to login page.");
      this.router.navigate(['/login']); // Asigură-te că '/login' este calea corectă
    }
  }

  handleLogout(): void {
    this.userService.logout(); // Serviciul se ocupă de logica de logout și navigare
    this.currentUser = null;   // Curăță datele utilizatorului din componentă
    this.initial='';
    this.loadMenuItems();      // Reîncarcă itemii meniului
  }

  logoutFromPanel(overlayPanelRef: OverlayPanel): void {
    this.handleLogout();
    if (overlayPanelRef) {
      overlayPanelRef.hide(); // Ascunde panoul după logout
    }
  }
}
