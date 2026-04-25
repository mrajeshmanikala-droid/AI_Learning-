import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-login.component.html',
  styles: [`
    :host {
      display: block;
      height: 100vh;
      background-color: #f7f9fb; /* surface */
    }
  `]
})
export class AdminLoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  credentials = {
    email: '',
    password: ''
  };

  error = '';
  isLoading = false;

  onSubmit() {
    this.isLoading = true;
    this.error = '';
    
    this.authService.loginRaw(this.credentials).subscribe({
      next: (res) => {
        this.isLoading = false;
        if (res.user.role !== 'ADMIN') {
          this.error = 'Access denied. Administrator privileges required.';
          this.authService.logout();
        } else {
          this.router.navigate(['/admin/dashboard']);
        }
      },
      error: (err) => {
        this.error = 'Invalid administrative credentials.';
        this.isLoading = false;
      }
    });
  }
}
