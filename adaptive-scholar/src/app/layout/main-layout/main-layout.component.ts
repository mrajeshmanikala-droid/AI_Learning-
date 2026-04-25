import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { TopbarComponent } from '../topbar/topbar.component';
import { BottomNavComponent } from '../bottom-nav/bottom-nav.component';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, SidebarComponent, TopbarComponent, BottomNavComponent],
  template: `
    <app-sidebar />
    <main class="md:ml-64 min-h-screen">
      <app-topbar />
      <router-outlet />
    </main>
    <app-bottom-nav />
  `
})
export class MainLayoutComponent {}
