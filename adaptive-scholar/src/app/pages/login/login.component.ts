import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styles: [`
    :host { display: block; }
    .editorial-gap { letter-spacing: -0.02em; }
    .ai-inner-glow { box-shadow: inset 0 0 10px rgba(201, 230, 255, 0.5); }
  `]
})
export class LoginComponent {
  credentials = { email: '', password: '' };
  errorMessage = '';
  isLoading = false;
  showPassword = false;

  // Forgot Password state
  showForgotPassword = false;
  forgotStep: 'email' | 'otp' | 'success' = 'email';
  forgotEmail = '';
  forgotOtp = '';
  forgotNewPassword = '';
  forgotConfirmPassword = '';
  forgotMessage = '';
  forgotError = '';
  forgotLoading = false;
  showNewPassword = false;

  private authService = inject(AuthService);

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';

    this.authService.login(this.credentials).subscribe({
      next: () => {
        this.isLoading = false;
        // Navigation handled in service
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error || 'Login failed. Please verify your credentials.';
      }
    });
  }

  // Forgot Password Flow
  openForgotPassword() {
    this.showForgotPassword = true;
    this.forgotStep = 'email';
    this.forgotEmail = this.credentials.email || '';
    this.forgotOtp = '';
    this.forgotNewPassword = '';
    this.forgotConfirmPassword = '';
    this.forgotMessage = '';
    this.forgotError = '';
  }

  closeForgotPassword() {
    this.showForgotPassword = false;
  }

  sendResetOtp() {
    if (!this.forgotEmail.trim()) {
      this.forgotError = 'Please enter your email address.';
      return;
    }
    this.forgotLoading = true;
    this.forgotError = '';
    this.forgotMessage = '';

    this.authService.sendForgotPasswordOtp(this.forgotEmail).subscribe({
      next: (res) => {
        this.forgotLoading = false;
        this.forgotMessage = res.message || 'OTP sent! Check your email.';
        this.forgotStep = 'otp';
      },
      error: (err) => {
        this.forgotLoading = false;
        this.forgotError = err.error?.message || 'Failed to send OTP. Please check your email and try again.';
      }
    });
  }

  resetPassword() {
    this.forgotError = '';

    if (!this.forgotOtp.trim()) {
      this.forgotError = 'Please enter the OTP code.';
      return;
    }
    if (this.forgotNewPassword.length < 6) {
      this.forgotError = 'Password must be at least 6 characters.';
      return;
    }
    if (this.forgotNewPassword !== this.forgotConfirmPassword) {
      this.forgotError = 'Passwords do not match.';
      return;
    }

    this.forgotLoading = true;

    this.authService.resetPassword(this.forgotEmail, this.forgotOtp, this.forgotNewPassword).subscribe({
      next: (res) => {
        this.forgotLoading = false;
        this.forgotMessage = res.message || 'Password reset successfully!';
        this.forgotStep = 'success';
      },
      error: (err) => {
        this.forgotLoading = false;
        this.forgotError = err.error?.message || 'Failed to reset password. Your OTP may be invalid or expired.';
      }
    });
  }
}
