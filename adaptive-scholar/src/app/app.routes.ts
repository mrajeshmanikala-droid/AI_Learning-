import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { adminGuard } from './guards/admin.guard';
import { courseResolver } from './resolvers/course.resolver';

export const routes: Routes = [
  // Admin routes (most specific, placed first)
  {
    path: 'admin/login',
    loadComponent: () => import('./pages/admin-login/admin-login.component').then(m => m.AdminLoginComponent)
  },
  {
    path: 'admin',
    canActivate: [adminGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./pages/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent) },
      { path: 'manage-courses', loadComponent: () => import('./pages/manage-courses/manage-courses.component').then(m => m.ManageCoursesComponent) },
      { path: 'add-course', loadComponent: () => import('./pages/add-course/add-course.component').then(m => m.AddCourseComponent) },
      { path: 'edit-course/:id', loadComponent: () => import('./pages/edit-course/edit-course.component').then(m => m.EditCourseComponent) },
    ]
  },
  // Auth routes (specific paths, NOT using empty-path parent)
  {
    path: 'login',
    component: AuthLayoutComponent,
    children: [
      { path: '', loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent) },
    ]
  },
  {
    path: 'register',
    component: AuthLayoutComponent,
    children: [
      { path: '', loadComponent: () => import('./pages/register/register.component').then(m => m.RegisterComponent) },
    ]
  },
  // Main app routes
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: 'dashboard', loadComponent: () => import('./pages/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { 
        path: 'courses', 
        loadComponent: () => import('./pages/course-catalog/course-catalog.component').then(m => m.CourseCatalogComponent),
        resolve: { courses: courseResolver }
      },
      { path: 'courses/:id', loadComponent: () => import('./pages/course-details/course-details.component').then(m => m.CourseDetailsComponent) },
      { path: 'ai-assistant', loadComponent: () => import('./pages/ai-assistant/ai-assistant.component').then(m => m.AiAssistantComponent) },
      { path: 'quiz/:id', loadComponent: () => import('./pages/ai-quiz/ai-quiz.component').then(m => m.AiQuizComponent) },
      { path: 'my-learning', loadComponent: () => import('./pages/notes-summary/notes-summary.component').then(m => m.NotesSummaryComponent) },
      { path: 'profile', loadComponent: () => import('./pages/user-profile/user-profile.component').then(m => m.UserProfileComponent) },
    ]
  },
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: '**', redirectTo: '/dashboard' }
];
