import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const adminGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check BehaviorSubject first
  if (authService.isAdmin()) {
    return true;
  }

  // Fallback: check localStorage directly.
  // This handles the race condition where loginRaw() stores the user
  // in localStorage but the BehaviorSubject hasn't propagated yet
  // when router.navigate fires immediately after login.
  try {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const user = JSON.parse(storedUser);
      if (user?.role === 'ADMIN') {
        // Sync the BehaviorSubject so future checks work immediately
        authService.syncUserFromStorage();
        return true;
      }
    }
  } catch (e) {}

  // Redirect to admin login if not admin
  router.navigate(['/admin/login']);
  return false;
};
