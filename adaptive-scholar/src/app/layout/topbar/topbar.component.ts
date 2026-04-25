import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { SearchService } from '../../services/search.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <header class="w-full h-16 flex justify-between items-center px-6 sticky top-0 z-40 bg-[#f7f9fb] border-b border-outline-variant/5">
      <div class="flex items-center flex-1 max-w-xl">
        <div class="relative w-full">
          <span class="absolute inset-y-0 left-0 pl-3 flex items-center text-slate-400">
            <span class="material-symbols-outlined">search</span>
          </span>
          <input type="text" 
                 [ngModel]="searchQuery()" 
                 (ngModelChange)="onSearchChange($event)"
                 placeholder="Search courses, notes, or AI insights..."
                 class="block w-full pl-10 pr-3 py-2 bg-surface-container-low border-none rounded-full text-sm focus:ring-2 focus:ring-primary/20 transition-all" />
          @if (searchQuery()) {
            <button (click)="clearSearch()" class="absolute inset-y-0 right-3 flex items-center text-slate-400 hover:text-primary transition-colors">
              <span class="material-symbols-outlined text-sm">close</span>
            </button>
          }
        </div>
      </div>
      <div class="flex items-center gap-4 ml-6">
        <button class="text-slate-600 hover:text-indigo-500 smooth-transition relative">
          <span class="material-symbols-outlined">notifications</span>
          <span class="absolute top-0 right-0 w-2 h-2 bg-error rounded-full"></span>
        </button>
        
        <!-- Profile Dropdown Container -->
        <div class="relative group" tabindex="0">
          <div class="w-8 h-8 rounded-full overflow-hidden bg-primary/10 ring-2 ring-primary/20 flex items-center justify-center font-bold text-primary cursor-pointer hover:bg-primary/20 transition-colors">
            <span *ngIf="user?.name" class="text-sm">{{ user?.name?.charAt(0) | uppercase }}</span>
            <span *ngIf="!user?.name" class="material-symbols-outlined text-[18px]">person</span>
          </div>
          
          <!-- Dropdown Menu -->
          <div class="absolute right-0 top-full mt-2 w-56 bg-white border border-outline-variant/20 rounded-xl shadow-xl opacity-0 invisible group-focus-within:opacity-100 group-focus-within:visible transition-all duration-200 z-50 overflow-hidden transform origin-top-right group-focus-within:scale-100 scale-95">
            <div class="p-4 border-b border-outline-variant/10 bg-surface-container-lowest flex items-center gap-3">
               <div class="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center font-bold text-primary flex-shrink-0">
                 {{ user?.name ? (user?.name?.charAt(0) | uppercase) : 'U' }}
               </div>
               <div class="overflow-hidden">
                 <p class="font-bold text-sm text-on-surface truncate">{{ user?.name || 'Guest User' }}</p>
                 <p class="text-xs text-on-surface-variant truncate">{{ user?.email || 'Not signed in' }}</p>
               </div>
            </div>
            <div class="p-2 flex flex-col">
              <a routerLink="/profile" class="px-3 py-2 text-sm text-on-surface hover:bg-surface-container-low rounded-lg transition-colors flex items-center gap-2 font-medium">
                <span class="material-symbols-outlined text-[18px] text-primary">person</span> My Profile
              </a>
              <a routerLink="/dashboard" class="px-3 py-2 text-sm text-on-surface hover:bg-surface-container-low rounded-lg transition-colors flex items-center gap-2 font-medium">
                <span class="material-symbols-outlined text-[18px] text-tertiary">dashboard</span> Dashboard
              </a>
              <div class="h-px bg-outline-variant/20 my-1 mx-2"></div>
              <button (click)="logout()" class="w-full px-3 py-2 text-sm text-error hover:bg-error/10 text-left rounded-lg transition-colors flex items-center gap-2 font-bold">
                <span class="material-symbols-outlined text-[18px]">logout</span> Sign Out
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  `
})
export class TopbarComponent {
  private authService = inject(AuthService);
  private searchService = inject(SearchService);
  private router = inject(Router);

  searchQuery = this.searchService.query;
  
  get user() {
    return this.authService.currentUser;
  }

  onSearchChange(query: string) {
    this.searchService.setQuery(query);
    if (query.trim() && !this.router.url.includes('/courses')) {
      this.router.navigate(['/courses']);
    }
  }

  clearSearch() {
    this.searchService.clearQuery();
  }
  
  logout() {
    this.authService.logout();
  }
}
