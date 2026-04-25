import { Component, inject, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, UserData } from '../../services/auth.service';
import { CourseService, Course } from '../../services/course.service';
import { ProgressService } from '../../services/progress.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-profile.component.html'
})
export class UserProfileComponent implements OnInit, OnDestroy {
  private authService = inject(AuthService);
  private courseService = inject(CourseService);
  private progressService = inject(ProgressService);
  private cdr = inject(ChangeDetectorRef);
  private sub!: Subscription;

  user: UserData | null = null;
  courses: Course[] = [];

  // Computed stats
  cognitiveProgress = 0;
  totalHours = 0;
  totalLessonsCompleted = 0;
  memberSince = '';

  // Edit modal state
  isEditModalOpen = false;
  editName = '';
  editBio = '';
  isSaving = false;
  saveSuccess = false;

  // Achievements (derived from course data)
  achievements: { icon: string; bgClass: string; title: string; description: string }[] = [];

  ngOnInit(): void {
    this.sub = this.authService.currentUser$.subscribe(u => {
      this.user = u;
      this.cdr.detectChanges();
    });

    this.courseService.getCourses().subscribe({
      next: (data) => {
        this.courses = data;

        // Overlay user-specific progress
        this.progressService.getUserProgress().subscribe({
          next: (progressList) => {
            const progressMap = new Map<number, number>();
            for (const p of progressList) {
              progressMap.set(p.courseId, p.progress);
            }
            for (const course of this.courses) {
              course.progress = progressMap.get(course.id) ?? 0;
            }
            this.computeStats();
            this.buildAchievements();
            this.cdr.detectChanges();
          },
          error: () => {
            // Default to 0 progress on error
            this.courses.forEach(c => c.progress = 0);
            this.computeStats();
            this.buildAchievements();
            this.cdr.detectChanges();
          }
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.sub?.unsubscribe();
  }

  private computeStats(): void {
    if (this.courses.length === 0) return;

    // Only count courses the user has actually started
    const startedCourses = this.courses.filter(c => c.progress > 0);

    if (startedCourses.length > 0) {
      const totalProgress = startedCourses.reduce((sum, c) => sum + c.progress, 0);
      this.cognitiveProgress = Math.round(totalProgress / startedCourses.length);

      const totalLessons = startedCourses.reduce((sum, c) => sum + (c.lessons?.length || 0), 0);
      this.totalLessonsCompleted = Math.round(totalLessons * (this.cognitiveProgress / 100));
      this.totalHours = Math.round(this.totalLessonsCompleted * 0.75);
    } else {
      this.cognitiveProgress = 0;
      this.totalLessonsCompleted = 0;
      this.totalHours = 0;
    }
  }

  private buildAchievements(): void {
    this.achievements = [];

    const completedCourses = this.courses.filter(c => c.progress >= 90);
    if (completedCourses.length > 0) {
      this.achievements.push({
        icon: 'psychology',
        bgClass: 'bg-tertiary-fixed',
        title: 'Deep Thinker',
        description: `Completed ${completedCourses.length} course(s) with 90%+ progress.`
      });
    }

    if (this.totalHours >= 10) {
      this.achievements.push({
        icon: 'timer',
        bgClass: 'bg-surface-container-highest',
        title: 'Dedicated Learner',
        description: `Invested ${this.totalHours}+ hours in active learning.`
      });
    }

    if (this.courses.length >= 2) {
      this.achievements.push({
        icon: 'diversity_3',
        bgClass: 'bg-primary-fixed',
        title: 'Multi-Disciplined',
        description: `Enrolled in ${this.courses.length} different courses simultaneously.`
      });
    }

    // Always show at least one
    if (this.achievements.length === 0) {
      this.achievements.push({
        icon: 'school',
        bgClass: 'bg-primary-fixed',
        title: 'New Scholar',
        description: 'Welcome! Complete your first lesson to unlock achievements.'
      });
    }
  }

  openEditModal(): void {
    this.editName = this.user?.name || '';
    this.editBio = this.user?.bio || '';
    this.saveSuccess = false;
    this.isEditModalOpen = true;
  }

  closeEditModal(): void {
    this.isEditModalOpen = false;
  }

  saveProfile(): void {
    if (!this.user?.email || this.isSaving) return;

    this.isSaving = true;
    this.saveSuccess = false;

    this.authService.updateProfile({
      email: this.user.email,
      name: this.editName,
      bio: this.editBio
    }).subscribe({
      next: () => {
        this.isSaving = false;
        this.saveSuccess = true;
        this.cdr.detectChanges();
        // Auto-close after 1.5 seconds
        setTimeout(() => {
          this.isEditModalOpen = false;
          this.cdr.detectChanges();
        }, 1500);
      },
      error: (err) => {
        console.error('Profile update failed', err);
        this.isSaving = false;
        this.cdr.detectChanges();
      }
    });
  }

  get initials(): string {
    const name = this.user?.name || 'GU';
    const parts = name.split(' ');
    return parts.map(p => p[0]).join('').substring(0, 2).toUpperCase();
  }
}
