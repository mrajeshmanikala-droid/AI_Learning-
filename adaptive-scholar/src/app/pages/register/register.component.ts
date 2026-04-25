import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styles: [`
    :host { display: block; }
    .glass-panel {
      background: rgba(255, 255, 255, 0.8);
      backdrop-filter: blur(24px);
    }
    .editorial-shadow {
      box-shadow: 0 20px 50px -12px rgba(25, 28, 30, 0.08);
    }
    .primary-gradient {
      background: linear-gradient(135deg, #4b41e1 0%, #712ae2 100%);
    }
  `]
})
export class RegisterComponent {
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  showPassword = false;
  errorMessage = '';
  isLoading = false;
  
  // OTP State
  isOtpSent = false;
  otpCode = '';

  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  /** Capitalize first letter of each word */
  private capitalizeWords(str: string): string {
    return str
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  get passwordStrength(): number {
    let strength = 0;
    if (this.password.length >= 8) strength++;
    if (/[0-9]/.test(this.password)) strength++;
    if (/[a-z]/.test(this.password) && /[A-Z]/.test(this.password)) strength++;
    if (/[^a-zA-Z0-9]/.test(this.password)) strength++;
    return strength;
  }

  get passwordStrengthLabel(): string {
    const labels = ['', 'Weak', 'Fair', 'Strong', 'Very Strong'];
    return labels[this.passwordStrength] || '';
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (!this.isOtpSent) {
      this.requestOtp();
    } else {
      this.verifyAndRegister();
    }
  }

  private requestOtp() {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.detectChanges();

    this.authService.sendRegistrationOtp(this.email).subscribe({
      next: () => {
        this.isLoading = false;
        this.isOtpSent = true;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error || 'Failed to send OTP. Please check your email and try again.';
        this.cdr.detectChanges();
      }
    });
  }

  private verifyAndRegister() {
    if (!this.otpCode || this.otpCode.length !== 6) {
      this.errorMessage = 'Please enter a valid 6-digit OTP code.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.detectChanges();

    const fullName = this.capitalizeWords(
      `${this.firstName.trim()} ${this.lastName.trim()}`.trim()
    );

    const userData = {
      name: fullName,
      email: this.email,
      password: this.password,
      otpCode: this.otpCode
    };

    this.authService.register(userData).subscribe({
      next: () => {
        this.isLoading = false;
        this.cdr.detectChanges();
        // Navigation handled in service
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error || 'Registration failed. Invalid or expired OTP.';
        this.cdr.detectChanges();
      }
    });
  }
}
