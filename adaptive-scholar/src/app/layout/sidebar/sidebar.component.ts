import { Component, Input } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive],
  template: `
    <aside class="hidden md:flex h-screen w-64 fixed left-0 top-0 bg-[#f2f4f6] flex-col py-8 px-4 gap-y-2 z-50">
      <div class="mb-10 px-2">
        <div class="flex items-center gap-3">
          <div class="w-10 h-10 bg-primary-container rounded-lg flex items-center justify-center">
            <span class="material-symbols-outlined text-on-primary-container">school</span>
          </div>
          <div>
            <h1 class="text-xl font-black text-indigo-700 font-headline leading-tight">Scholar</h1>
            <p class="text-[10px] uppercase tracking-widest text-slate-500 font-bold">AI-Powered Learning</p>
          </div>
        </div>
      </div>

      <nav class="flex-1 space-y-1">
        <a routerLink="/dashboard" routerLinkActive="text-indigo-700 font-bold bg-white/50 translate-x-1"
           class="flex items-center gap-3 px-3 py-2 text-sm font-medium text-slate-500 hover:bg-white/30 smooth-transition rounded-lg">
          <span class="material-symbols-outlined">dashboard</span>
          <span>Dashboard</span>
        </a>
        <a routerLink="/courses" routerLinkActive="text-indigo-700 font-bold bg-white/50 translate-x-1"
           class="flex items-center gap-3 px-3 py-2 text-sm font-medium text-slate-500 hover:bg-white/30 smooth-transition rounded-lg">
          <span class="material-symbols-outlined">school</span>
          <span>Courses</span>
        </a>
        <a routerLink="/my-learning" routerLinkActive="text-indigo-700 font-bold bg-white/50 translate-x-1"
           class="flex items-center gap-3 px-3 py-2 text-sm font-medium text-slate-500 hover:bg-white/30 smooth-transition rounded-lg">
          <span class="material-symbols-outlined">local_library</span>
          <span>My Learning</span>
        </a>
        <a routerLink="/ai-assistant" routerLinkActive="text-indigo-700 font-bold bg-white/50 translate-x-1"
           class="flex items-center justify-between px-3 py-2 text-sm font-medium text-slate-500 hover:bg-white/30 smooth-transition rounded-lg relative">
          <div class="flex items-center gap-3">
            <span class="material-symbols-outlined">smart_toy</span>
            <span>AI Assistant</span>
          </div>
          <span class="bg-primary text-on-primary text-[8px] font-black px-1.5 py-0.5 rounded-full animate-pulse">NEW</span>
        </a>
        <a routerLink="/profile" routerLinkActive="text-indigo-700 font-bold bg-white/50 translate-x-1"
           class="flex items-center gap-3 px-3 py-2 text-sm font-medium text-slate-500 hover:bg-white/30 smooth-transition rounded-lg">
          <span class="material-symbols-outlined">person</span>
          <span>Profile</span>
        </a>
      </nav>

      <div class="mt-auto pt-6 border-t border-outline-variant/10">
        <a routerLink="/login"
           class="flex items-center gap-3 px-3 py-2 text-sm font-medium text-slate-500 hover:bg-white/30 smooth-transition rounded-lg">
          <span class="material-symbols-outlined">logout</span>
          <span>Logout</span>
        </a>
      </div>
    </aside>
  `
})
export class SidebarComponent {}
