import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';

export interface UserData {
  id: number;
  name: string;
  email: string;
  role: string;
  bio?: string;
}

export interface AuthResponse {
  token: string;
  user: UserData;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  private readonly API_URL = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<UserData | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        this.currentUserSubject.next(JSON.parse(storedUser));
      } catch(e) {}
    }
  }

  get currentUser() {
    return this.currentUserSubject.value;
  }

  isAdmin() {
    return this.currentUser?.role === 'ADMIN';
  }

  /**
   * Re-reads user data from localStorage into the BehaviorSubject.
   * Used by the admin guard to fix race conditions after login.
   */
  syncUserFromStorage() {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        this.currentUserSubject.next(JSON.parse(storedUser));
      } catch (e) {}
    }
  }

  getAuthHeaders() {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  login(credentials: {email: string, password: string}): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          this.handleAuthOperation(response);
          if (response.user.role === 'ADMIN') {
            this.router.navigate(['/admin/dashboard']);
          } else {
            this.router.navigate(['/dashboard']);
          }
        })
      );
  }

  /**
   * Authenticate without auto-navigating. Used by admin login to control flow.
   */
  loginRaw(credentials: {email: string, password: string}): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          this.handleAuthOperation(response);
        })
      );
  }

  sendRegistrationOtp(email: string): Observable<{message: string}> {
    return this.http.post<{message: string}>(`${this.API_URL}/send-otp`, { email });
  }

  register(userData: {name: string, email: string, password: string, otpCode: string}): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, userData)
      .pipe(
        tap(response => {
          this.handleAuthOperation(response);
          this.router.navigate(['/dashboard']);
        })
      );
  }

  updateProfile(updates: { email: string; name?: string; bio?: string }): Observable<AuthResponse> {
    const token = localStorage.getItem('token');
    let headers = new HttpHeaders();
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return this.http.put<AuthResponse>(`${this.API_URL}/profile`, updates, { headers })
      .pipe(
        tap(response => {
          this.handleAuthOperation(response);
        })
      );
  }

  sendForgotPasswordOtp(email: string): Observable<{message: string}> {
    return this.http.post<{message: string}>(`${this.API_URL}/forgot-password`, { email });
  }

  resetPassword(email: string, otpCode: string, newPassword: string): Observable<{message: string}> {
    return this.http.post<{message: string}>(`${this.API_URL}/reset-password`, { email, otpCode, newPassword });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  private handleAuthOperation(response: AuthResponse) {
    if (response && response.token) {
      localStorage.setItem('token', response.token);
      localStorage.setItem('user', JSON.stringify(response.user));
      this.currentUserSubject.next(response.user);
    }
  }
}
