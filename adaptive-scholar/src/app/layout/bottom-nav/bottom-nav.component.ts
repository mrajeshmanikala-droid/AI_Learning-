import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-bottom-nav',
  imports: [RouterLink, RouterLinkActive],
  template: `
    <nav class="md:hidden fixed bottom-0 left-0 right-0 h-16 bg-white glass-panel border-t border-outline-variant/10 flex items-center justify-around px-4 z-50">
      <a routerLink="/dashboard" routerLinkActive="text-indigo-700" class="flex flex-col items-center gap-1 text-slate-400">
        <span class="material-symbols-outlined">dashboard</span>
        <span class="text-[10px] font-bold">Dash</span>
      </a>
      <a routerLink="/courses" routerLinkActive="text-indigo-700" class="flex flex-col items-center gap-1 text-slate-400">
        <span class="material-symbols-outlined">school</span>
        <span class="text-[10px] font-bold">Courses</span>
      </a>
      <a routerLink="/ai-assistant" routerLinkActive="text-indigo-700" class="flex flex-col items-center gap-1 text-slate-400">
        <span class="material-symbols-outlined">smart_toy</span>
        <span class="text-[10px] font-bold">AI</span>
      </a>
      <a routerLink="/profile" routerLinkActive="text-indigo-700" class="flex flex-col items-center gap-1 text-slate-400">
        <span class="material-symbols-outlined">person</span>
        <span class="text-[10px] font-bold">Profile</span>
      </a>
    </nav>
  `
})
export class BottomNavComponent {}
